package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.Cart;
import com.innovature.rentx.entity.Product;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.CartForm;
import com.innovature.rentx.repository.CartRepository;
import com.innovature.rentx.repository.UserRepository;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.service.CartService;
import com.innovature.rentx.util.CategoryUtil;
import com.innovature.rentx.util.DateValidation;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.UserCartListView;
import com.innovature.rentx.view.UserCartProduListView;
import com.innovature.rentx.util.CartUtil;
import com.innovature.rentx.util.ProductUtil;
import com.innovature.rentx.view.UserCartView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final LanguageUtil languageUtil;

    private final CategoryUtil categoryUtil;

    private final ProductUtil productUtil;

    private final CartUtil cartUtil;

    private final DateValidation dateValidation;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private static final String UNABLE_PERFORM = "unable.to.perform.this.action";
    private static final String PAGE_NUMBER = "error.page.number.natural";

    private static final String SIZE_NUMBER = "error.page.size.natural";
    private static final String SORT_INVALID = "sort.invalid";
    private static final String DIRECTION_INVALID = "direction.invalid";

    private static final String INVALID_START_DATE = "start.date.validation";

    private static final String INVALID_END_DATE = "end.date.validation";

    @Override
    public UserCartView save(CartForm form) {

        Cart cart;
        dateValidation.verifyDateExists(form.getStartDate(), INVALID_START_DATE);
        dateValidation.verifyDateExists(form.getEndDate(), INVALID_END_DATE);

        Date startDate = dateValidation.stringToDateFormat(form.getStartDate(), INVALID_START_DATE);
        Date endDate = dateValidation.stringToDateFormat(form.getEndDate(), INVALID_END_DATE);
        dateValidation.dateCheck(startDate, endDate);

        User user = userRepository.findByIdAndStatusAndRole(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value,
                User.Role.USER.value);
        Product product = productUtil.validateProduct(form.getProductId());
        productUtil.validateQuantity(product, form.getQuantity());

        cart = cartUtil.validateItemExists(form);

        if (cart == null) {
            cart = new Cart(form, startDate, endDate, product, user);

        }
        cartRepository.save(cart);

        int cartCount = cartRepository.countByUserIdAndStatus(SecurityUtil.getCurrentUserId(),
                Cart.Status.ACTIVE.value);

        return new UserCartView(cart, cartCount);
    }

    @Override
    public UserCartView modifyCart(Integer cartId, Integer quantity, String startDate, String endDate) {
        if (startDate == null && endDate == null && quantity == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_PERFORM, null, "en"));

        }
        Cart cart = cartUtil.validateCartId(cartId);
        Product product = productUtil.validateProduct(cart.getProduct().getId());

        if (quantity != null) {
            productUtil.validateQuantity(product, quantity);
            cart.setQuantity(quantity);

        }
        if (startDate != null && endDate != null) {
            Date startDates = dateValidation.stringToDateFormat(startDate, INVALID_START_DATE);
            Date endDates = dateValidation.stringToDateFormat(endDate, INVALID_END_DATE);
            dateValidation.dateCheck(startDates, endDates);
            cart.setStartDate(startDates);
            cart.setEndDate(endDates);

        } else if (startDate != null) {
            Date startDates = dateValidation.stringToDateFormat(startDate, INVALID_START_DATE);
            dateValidation.dateCheck(startDates, cart.getEndDate());
            cart.setStartDate(startDates);

        } else if (endDate != null) {
            Date endDates = dateValidation.stringToDateFormat(endDate, INVALID_END_DATE);
            dateValidation.dateCheck(cart.getStartDate(), endDates);
            cart.setEndDate(endDates);
        }

        cartRepository.save(cart);
        int cartCount = cartRepository.countByUserIdAndStatus(SecurityUtil.getCurrentUserId(),
                Cart.Status.ACTIVE.value);
        return new UserCartView(cart, cartCount);

    }

    public ResponseEntity<String> removeItem(Integer cartId) {

        Cart cart = cartUtil.validateCartId(cartId);
        cart.setStatus(Cart.Status.DELETED.value);
        cartRepository.save(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public UserCartListView viewCartProduct(Integer page, Integer size, String sort, String order) {
        double totalPrice = 0.0;
        boolean orderD = !order.equalsIgnoreCase("asc");

        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);

        Pageable pageable = PageRequest.of(page - 1, size, (orderD) ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        List<Cart> getTotalPrice = cartRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(),
                Cart.Status.ACTIVE.value);
        Page<Cart> cartPage = cartRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(),
                Cart.Status.ACTIVE.value, pageable);
        List<Cart> result = cartPage.getContent();
        for (Cart cart : getTotalPrice) {
            int dateDiff = dateValidation.checkDateDifferenceInMillis(cart.getStartDate(), cart.getEndDate());
            double price = cart.getProduct().getPrice();
            int quantity = cart.getQuantity();
            totalPrice += (price * dateDiff) * quantity;

        }

        int count = cartRepository.countByUserIdAndStatus(SecurityUtil.getCurrentUserId(), Cart.Status.ACTIVE.value);

        List<UserCartProduListView> cartViewList = result.stream()
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
                    if (countStart < 0) {
                        isAvailable = false;

                        return new UserCartProduListView(cart, isAvailable, false);
                    }
                    return new UserCartProduListView(cart, isAvailable, true);
                })
                .collect(Collectors.toList());

        Pager<UserCartProduListView> pager = new Pager<>(size, count, page);
        pager.setResult(cartViewList);

        UserCartListView userCartListView = new UserCartListView();
        userCartListView.setCartProducts(pager);
        userCartListView.setTotalPrice(totalPrice);

        return userCartListView;
    }

}
