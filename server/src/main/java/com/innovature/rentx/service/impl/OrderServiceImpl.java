package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.*;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.OrderForm;
import com.innovature.rentx.form.OrderPreSummary;
import com.innovature.rentx.repository.*;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.service.OrderService;
import com.innovature.rentx.util.*;
import com.innovature.rentx.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartUtil cartUtil;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private ProductUtil productUtil;
    @Autowired
    private CategoryUtil categoryUtil;
    @Autowired
    private DateValidation dateValidation;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageUtil languageUtil;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private static final String UNABLE_PERFORM = "unable.to.perform.this.action";
    private static final String PAGE_NUMBER = "error.page.number.natural";

    private static final String SIZE_NUMBER = "error.page.size.natural";
    private static final String SORT_INVALID = "sort.invalid";
    private static final String DIRECTION_INVALID = "direction.invalid";
    private static final String INVALID_ORDER_PRODUCT_ID = "invalid.order.product.id";
    private static final String STORE = "store";
    private static final String USER = "user";
    private static final String ROLE = "role";
    private static final String STATUS = "status";
    private static final String ID = "id";

    Double vatPrice = 10.0;
    private final byte[] orderProductStatusCheck={
            OrderProduct.Status.ACTIVE.value,
            OrderProduct.Status.INACTIVE.value,
            OrderProduct.Status.COMPLETED.value,
            OrderProduct.Status.DELETED.value
    };
    private final byte[] orderMasterStatusCheck= {
            OrderProductMaster.Status.ACTIVE.value,
            OrderProductMaster.Status.INACTIVE.value
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final EntityManager entityManager;
    public OrderServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public ResponseEntity<String> addOrder(OrderForm form) {

        User user = userUtil.validateUser(UNABLE_PERFORM);
        LOGGER.info("{} added order", user.getEmail());

        Boolean validationSuccess = loopCartData(form, user);
        if (Boolean.TRUE.equals(validationSuccess)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to process", HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean loopCartData(OrderForm form, User user) {
        double grandTotal = 0.0;
        Address address = addressRepository.findByIdAndStatus(form.getAddressId(), Address.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText("invalid.address.id", null, "en")));
        PaymentMethod paymentMethod = paymentRepository.findByIdAndStatus(form.getPaymentId(),
                PaymentMethod.Status.ACTIVE.value);

        if (paymentMethod == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.payment.type", null, "en"));
        }
        OrderProductMaster orderProductMaster = new OrderProductMaster();
        orderProductMaster.setProductCount(form.getCartId().size());
        orderProductMaster.setStatus(OrderProductMaster.Status.INACTIVE.value);
        orderProductMaster.setAddress(address);
        orderProductMaster.setUser(user);
        orderProductMaster.setPaymentMethod(paymentMethod);
        OrderProductMaster orderProductMaster1 = orderMasterRepository.save(orderProductMaster);
        for (Integer cartDataId : form.getCartId()) {
            double priceProduct;

            Cart cart = cartUtil.validateCartId(cartDataId);
            Product product = productUtil.validateProduct(cart.getProduct().getId());
            productUtil.validateQuantity(product, cart.getQuantity());
            int dateDiff = dateValidation.checkDateDifferenceInMillis(cart.getStartDate(), cart.getEndDate());
            LOGGER.info("{} Difference in total Date", dateDiff);

            Boolean checkAvailability = dateValidation.productAvailabilityCheck(cart.getProduct(), cart.getStartDate(),
                    cart.getEndDate(), cart.getQuantity());
            if (Boolean.TRUE.equals(checkAvailability)) {
                priceProduct = (cart.getProduct().getPrice() * dateDiff) * cart.getQuantity();
                grandTotal = grandTotal + priceProduct;
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(cart.getQuantity());
                orderProduct.setStartDate(cart.getStartDate());
                orderProduct.setEndDate(cart.getEndDate());
                orderProduct.setStatus(OrderProduct.Status.ACTIVE.value);
                orderProduct.setTotalPrice(priceProduct);
                orderProduct.setOrderProductMaster(orderProductMaster1);
                orderRepository.save(orderProduct);
                LOGGER.info("Order {}", orderProduct.getId());
                cart.setStatus(Cart.Status.DELETED.value);
                cartRepository.save(cart);
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText("unable.place.order", null, "en"));
            }

        }
        double calculateVat = grandTotal / vatPrice;
        double payableAmount = grandTotal + calculateVat;
        orderProductMaster.setGrantTotal(payableAmount);
        orderProductMaster.setStatus(OrderProductMaster.Status.ACTIVE.value);
        orderMasterRepository.save(orderProductMaster);

        return true;
    }

    public UserOrderSummaryPageView orderSummeryList(OrderPreSummary form, Integer page, Integer size, String sort,
            String order) {

        double totalPrice = 0.0;
        boolean orderD = !order.equalsIgnoreCase("asc");

        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);

        Pageable pageable = PageRequest.of(page - 1, size, (orderD) ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        List<Cart> listCartItem = cartRepository.findByUserIdAndIdInAndStatus(SecurityUtil.getCurrentUserId(),
                form.getCartId(), Cart.Status.ACTIVE.value);
        Page<Cart> cartPage = cartRepository.findByUserIdAndIdInAndStatus(SecurityUtil.getCurrentUserId(),
                form.getCartId(), Cart.Status.ACTIVE.value, pageable);

        for (Cart cart : listCartItem) {
            int dateDiff = dateValidation.checkDateDifferenceInMillis(cart.getStartDate(), cart.getEndDate());
            double price = cart.getProduct().getPrice();
            int quantity = cart.getQuantity();
            totalPrice += (price * dateDiff) * quantity;

        }

        List<Cart> result = cartPage.getContent();
        int count = result.size();
        List<UserOrderSummeryListProduct> cartViewList = result.stream()
                .map(cart -> {
                    Boolean isAvailable = dateValidation.productAvailabilityCheck(
                            cart.getProduct(),
                            cart.getStartDate(),
                            cart.getEndDate(),
                            cart.getQuantity());

                    Date currentDate = new Date();
                    Date dt = null;
                    Date startDate = cart.getStartDate();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String curDate = dateFormat.format(currentDate);

                    try {
                        dt = dateFormat.parse(curDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int countStart = startDate.compareTo(dt);
                    int dateDiff = dateValidation.checkDateDifferenceInMillis(cart.getStartDate(), cart.getEndDate());
                    double price = cart.getProduct().getPrice();
                    int quantity = cart.getQuantity();
                    double singleProductPrice = (price * dateDiff) * quantity;
                    if (countStart < 0) {
                        isAvailable = false;
                        return new UserOrderSummeryListProduct(cart, isAvailable, dateDiff, singleProductPrice);
                    }

                    return new UserOrderSummeryListProduct(cart, isAvailable, dateDiff, singleProductPrice);
                })
                .collect(Collectors.toList());

        double calculateVat = totalPrice / vatPrice;
        double payableAmount = totalPrice + calculateVat;

        Pager<UserOrderSummeryListProduct> pager = new Pager<>(size, count, page);
        pager.setResult(cartViewList);
        UserOrderSummaryPageView userCartListView = new UserOrderSummaryPageView();
        userCartListView.setOrderSummeryListProductPager(pager);
        userCartListView.setTotalPrice(totalPrice);
        userCartListView.setPriceIncludeVat(payableAmount);
        return userCartListView;
    }

    // scheduler for automatically restore the complted rentals
    @Override
    @Transactional
//    @Scheduled(cron = "0 0 0 * * *")
     @Scheduled(cron = "0/10 * * * * *")
    public void restore() {
        Date curDate = dateFormatter();

        Collection<OrderProduct> orderProduct = orderRepository.findByStatusAndEndDateLessThan(
                OrderProduct.Status.ACTIVE.value,
                curDate);

        for (OrderProduct order : orderProduct) {

            order.setStatus(OrderProduct.Status.COMPLETED.value);
            orderRepository.save(order);
        }
    }

    // Return current date as yyyy-mm-dd
    public Date dateFormatter() {

        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateTime.format(formatter);

        LocalDate localDate = LocalDate.parse(formattedDate, formatter);
        Date date = java.sql.Date.valueOf(localDate);
        return date;

    }

    public UserOrderDetailView detailView(Integer orderProductId) {
        Boolean isCancellable=false;

        OrderProduct orderProduct = orderRepository.findById(orderProductId).orElseThrow(
                () -> new BadRequestException(
                        languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en")));

        validateOrder(orderProduct);
        Integer days = dateValidation.checkDateDifferenceInMillis(orderProduct.getStartDate(), orderProduct.getEndDate());

        Date curDate = dateFormatter();
        if (curDate.before(orderProduct.getStartDate())) {
            isCancellable=true;
        }
        double calculateVat=productUtil.calculateVat(orderProduct.getTotalPrice());
        double payableAmount = orderProduct.getTotalPrice() + calculateVat;
        return new UserOrderDetailView(orderProduct, days,isCancellable,payableAmount);
    }

    public ResponseEntity<String> userCancelOrder(Integer orderProductId) {

        OrderProduct orderProduct = orderRepository.findByStatusAndId(OrderProduct.Status.ACTIVE.value, orderProductId)
                .orElseThrow(
                        () -> new BadRequestException(
                                languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en")));

        validateOrder(orderProduct);

        Date curDate = dateFormatter();

        if (curDate.before(orderProduct.getStartDate())) {
            
            double quantity = orderProduct.getQuantity();
            double price = orderProduct.getProduct().getPrice();
            
            double cancelItemPrice = price * quantity;
            double calculateVat = cancelItemPrice / vatPrice;
            double payableAmount = cancelItemPrice + calculateVat;


            orderProduct.setStatus(OrderProduct.Status.DELETED.value);
            orderRepository.save(orderProduct);

            OrderProductMaster orderProductMaster = orderMasterRepository
                    .findById(orderProduct.getOrderProductMaster().getId()).orElseThrow(
                            () -> new BadRequestException(
                                    languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en")));

            orderProductMaster.setProductCount(orderProductMaster.getProductCount() - 1);
            orderProductMaster.setGrantTotal(orderProductMaster.getGrantTotal() - payableAmount);
            orderMasterRepository.save(orderProductMaster);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BadRequestException(
                    languageUtil.getTranslatedText("unable.to.cancel.order", null, "en"));
        }

    }

    /////////////////////// VALIDATORS //////////////////
    private void validateOrder(OrderProduct orderProduct) {

        Integer userId = 0;
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(
                () -> new BadRequestException(
                        languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en")));

        if (user.getRole() == User.Role.USER.value) {

            userId = orderProduct.getOrderProductMaster().getUser().getId();

        } else if (user.getRole() == User.Role.VENDOR.value) {

            userId = orderProduct.getProduct().getStore().getUser().getId();
        }

        if (!Objects.equals(SecurityUtil.getCurrentUserId(), userId)) {

            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en"));

        }
    }

    @Override
    public Pager<UserOrderListView> OrderListViewPager(Integer page, Integer size, String sort, String order,String searchData){
        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);
        if (searchData == null) {
            searchData = "";
        } else {
            searchData = searchData.trim();
        }

        Page<OrderProduct> totalProduct ;
        List<Integer>orderMasterId=new ArrayList<>();
        boolean orderD = !order.equalsIgnoreCase("asc");
        List<OrderProductMaster> masterList=orderMasterRepository.findByUserIdAndStatusIn(SecurityUtil.getCurrentUserId(),orderMasterStatusCheck);

        for (OrderProductMaster orderProductMaster : masterList
        ) {
            orderMasterId.add(orderProductMaster.getId());

        }
        Integer[] masterIdArray = orderMasterId.toArray(new Integer[0]);
        totalProduct=orderRepository.findAllByOrderProductMasterIdInAndStatusInAndProductNameContainingIgnoreCase(
                orderMasterId,
          orderProductStatusCheck,
          searchData,
                PageRequest.of(page - 1, size, (orderD == true) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sort)
        );
        int count=orderRepository.countByOrderProductMasterIdInAndStatusInAndProductNameContainingIgnoreCase(orderMasterId,
                orderProductStatusCheck,
                searchData);
        List<UserOrderListView>orderList=totalProduct.stream()
                .map(orderProduct->{
                    int dateDiff = dateValidation.checkDateDifferenceInMillis(orderProduct.getStartDate(), orderProduct.getEndDate());
                    return new UserOrderListView(orderProduct,dateDiff);
                })
                .collect(Collectors.toList());
        Pager<UserOrderListView>pager = new Pager<>(size,count, page);
        pager.setResult(orderList);
        return pager;

    }


    public Pager<VendorOrderListView>vendorOrderListViewPager(Integer page, Integer size, String sort, String order,String searchData){
        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);
        boolean orderD = !order.equalsIgnoreCase("asc");
        if (searchData == null) {
            searchData = "";
        } else {
            searchData = searchData.trim();
        }
        List<VendorOrderListView>orderedProductList;
        Pager<VendorOrderListView> viewOrderedProductList;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderProduct> criteriaQuery = criteriaBuilder.createQuery(OrderProduct.class);
        Root<OrderProduct> orderProductRoot = criteriaQuery.from(OrderProduct.class);
        List<Predicate> predicate = new ArrayList<>();

        Join<OrderProduct, Product> productJoin = orderProductRoot.join("product");

        Join<Product, Store> storeJoin = productJoin.join(STORE, JoinType.INNER);
        Join<Store, User> userJoin = storeJoin.join(USER, JoinType.INNER);

        Predicate predicateUserId = criteriaBuilder.equal(userJoin.get(ID), SecurityUtil.getCurrentUserId());

        Predicate predicateUserRole = criteriaBuilder.equal(userJoin.get(ROLE), User.Role.VENDOR.value);
        Predicate predicateUserStatus = criteriaBuilder.equal(userJoin.get(STATUS), User.Status.ACTIVE.value);

        predicate.add(predicateUserId);
        predicate.add(predicateUserStatus);
        predicate.add(predicateUserRole);

        if(!StringUtils.isEmpty(searchData)){
            Predicate predicateSearch =criteriaBuilder.like(criteriaBuilder.lower(orderProductRoot.get("product").get("name")),
                    "%" + searchData + "%");
            predicate.add(predicateSearch);
        }

        Predicate whereCondition = criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));
        criteriaQuery.where(whereCondition);
        if (sort != null) {

            if (order.equalsIgnoreCase("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(orderProductRoot.get(sort)));
                criteriaQuery.orderBy(criteriaBuilder.asc(productJoin.get(sort)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(orderProductRoot.get(sort)));
                criteriaQuery.orderBy(criteriaBuilder.desc(productJoin.get(sort)));
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText("item.invalid.sort", null, "en"));
        }
        TypedQuery<OrderProduct> list = entityManager.createQuery(criteriaQuery);
        list.setFirstResult((page - 1) * size);
        list.setMaxResults(size);

        Long count;
        count = (long) entityManager.createQuery(criteriaQuery).getResultList().size();

        Page<OrderProduct> result = new PageImpl<>(list.getResultList(),
                PageRequest.of(page - 1, size, (orderD == true) ? Sort.Direction.DESC : Sort.Direction.ASC, sort),
                count);

        orderedProductList= StreamSupport.stream(result.spliterator(),false)
                .map(orderProduct -> {
                    OrderProductMaster orderProductMaster = orderMasterRepository
                            .findById(orderProduct.getOrderProductMaster().getId()).orElseThrow(
                                    () -> new BadRequestException(
                                            languageUtil.getTranslatedText(INVALID_ORDER_PRODUCT_ID, null, "en")));
                    return new VendorOrderListView(orderProduct,orderProductMaster);
                }).collect(Collectors.toList());
        viewOrderedProductList= new Pager<>(size, count.intValue(), page);
        viewOrderedProductList.setResult(orderedProductList);
        return viewOrderedProductList;



    }


//    @Override
//    public Pager<UserOrderListView> OrderListViewPager(Integer page, Integer size, String sort, String order, String searchData) {
//        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
//                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);
//
//        if (searchData == null) {
//            searchData = "";
//        } else {
//            searchData = searchData.trim();
//        }
//
//        List<OrderProduct> totalProduct ;
//        List<Integer> orderMasterId = new ArrayList<>();
//
//        List<OrderProductMaster> masterList = orderMasterRepository.findByUserIdAndStatusIn(SecurityUtil.getCurrentUserId(),orderMasterStatusCheck);
//
//        for (OrderProductMaster orderProductMaster : masterList) {
//            orderMasterId.add(orderProductMaster.getId());
//        }
//
//        List<UserOrderListView> orderList = new ArrayList<>();
//        if (!orderMasterId.isEmpty()) {
//            totalProduct = orderRepository.findAllByOrderProductMasterIdInAndStatusInAndProductNameContainingIgnoreCase(
//                    orderMasterId, orderProductStatusCheck, searchData
//            );
//
//            int count = totalProduct.size();
//            System.out.println("count: " + count);
//
//            orderList = totalProduct.stream()
//                    .map(orderProduct -> {
//                        System.out.println("Order" + orderProduct.getId());
//                        int dateDiff = dateValidation.checkDateDifferenceInMillis(orderProduct.getStartDate(), orderProduct.getEndDate());
//                        return new UserOrderListView(orderProduct, dateDiff);
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        Pager<UserOrderListView> pager = new Pager<>(size, orderList.size(), page);
//        pager.setResult(orderList);
//        return pager;
//    }

}
