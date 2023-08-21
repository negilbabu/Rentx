package com.innovature.rentx.service.impl;

import javax.transaction.Transactional;

import com.innovature.rentx.entity.*;
import com.innovature.rentx.repository.*;
import com.innovature.rentx.service.NearBySearchService;
import com.innovature.rentx.util.*;
import com.innovature.rentx.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.ImageProductForm;
import com.innovature.rentx.form.ProductForm;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.service.ProductService;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ImageProductRepository imageProductRepository;

    @Autowired
    LanguageUtil languageUtil;

    @Autowired
    private CartUtil cartUtil;
    @Autowired
    private WishlistUtil wishlistUtil;

    @Autowired
    private DateValidation dateValidation;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private NearBySearchService nearBySearchService;

    @Autowired
    private ProductUtil productUtil;
    @Autowired
    private CategoryUtil categoryUtil;

    private static final String UNABLE_TO_PERFORM = "unable.to.perform.this.action";
    private static final String INVALID_PRODUCT_ID = "invalid.product.id";
    private static final String INVALID_START_DATE = "start.date.validation";
    private static final String INVALID_END_DATE = "end.date.validation";
    private static final String PERMISSION_DENIED = "dont.have.access";
    private static final String CATEGORY_INVALID = "category.filter.invalid";
    private static final String STORE_INVALID = "store.filter.invalid";
    private static final String SEPARATOR = ",";
    private static final String CATEGORY = "category";
    private static final String STORE = "store";
    private static final String USER = "user";
    private static final String ROLE = "role";
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String ID = "id";

    private static final String PAGE_NUMBER = "error.page.number.natural";
    private static final String SIZE_NUMBER = "error.page.size.natural";
    private static final String SORT_INVALID = "sort.invalid";
    private static final String DIRECTION_INVALID = "direction.invalid";
    private byte[] wishlistExists = { Wishlist.STATUS.ACTIVE.value };

    private final EntityManager entityManager;

    private byte[] productAcceptedStatus;

    private byte[] imageProductAcceptedStatus;

    public ProductServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ProductView createProduct(ProductForm form) {

        final byte[] storeStatuses = { Store.Status.ACTIVE.value };
        userUtil.validateVendorCheck(PERMISSION_DENIED);
        User user = userRepository.findByIdAndStatus(SecurityUtil.getCurrentUserId(), Store.Status.ACTIVE.value);

        if (user.getStatus() != User.Status.ACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.blocked", null, "en"));
        } else {
            Category category = categoryUtil.categoryValidate(form.getCategory());

            SubCategory subCategory = categoryUtil.subCategoryValidate(form.getSubCategory(), form.getCategory());

            Store store = productUtil.storeValidate(form.getStore(), storeStatuses);

            if (productRepository.existsByNameAndStoreId(form.getName(), form.getStore())) {
                throw new BadRequestException(
                        languageUtil.getTranslatedText("Product-Name.already.exists", null, "en"));
            } else {
                return new ProductView(productRepository.save(new Product(form, category, subCategory, store)));
            }
        }
    }

    @Transactional
    public ResponseEntity<String> addImageProduct(ImageProductForm form) {

        userUtil.validateVendorCheck(PERMISSION_DENIED);
        Product product = productRepository.findById(form.getProductId());
        if (product != null) {

            switch (product.getStatus()) {
                case 0 -> {
                    throw new BadRequestException(
                            languageUtil.getTranslatedText("image.exists.already", null, "en"));
                }
                case 1 -> {
                    imageProductRepository.save(new ImageProduct(form));
                    product.setStatus(Product.Status.ACTIVE.value);
                }
                case 2 -> {
                    throw new BadRequestException(
                            languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"));
                }
                default -> throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
            }

        } else {
            throw new BadRequestException(
                    languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"));
        }

        productRepository.save(product);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteProduct(Integer productId) {

        try {
            Product product = productRepository.findById(productId);
            product.setStatus(Product.Status.DELETED.value);

            ImageProduct image = imageProductRepository.findByProductId(productId);
            image.setStatus(ImageProduct.Status.DELETED.value);

            productRepository.save(product);
            imageProductRepository.save(image);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"));
        }

    }

    @Transactional
    @Override
    public ResponseEntity<String> updateProduct(Integer productId, ProductForm form) {

        userUtil.validateVendorCheck(UNABLE_TO_PERFORM);

        final byte[] storeStatuses = { Store.Status.ACTIVE.value };

        User user = userRepository.findByIdAndStatus(SecurityUtil.getCurrentUserId(), Store.Status.ACTIVE.value);

        if (user.getStatus() != User.Status.ACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
        } else {
            Category category = categoryUtil.categoryValidate(form.getCategory());

            SubCategory subCategory = categoryUtil.subCategoryValidate(form.getSubCategory(), form.getCategory());

            Store store = productUtil.storeValidate(form.getStore(), storeStatuses);

            productUtil.checkProductNameExist(productId, form.getName());
            productUtil.saveProduct(productId, form, category, subCategory, store);

            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @Override
    public Pager<UserProductView> viewProduct(String searchData, Integer page, Integer size, String sort, String order,
            String CategoryId, String StoreId, String startDate, String endDate, Integer quantity, Double latitude,
            Double longitude) {

        Date formattedStartDate = dateValidation.stringToDateFormat(startDate, INVALID_START_DATE);
        Date formattedEndDate = dateValidation.stringToDateFormat(endDate, INVALID_END_DATE);
        Double radius = 6371.00;
        List<Store> getNearByStores;
        boolean orderD = !order.equalsIgnoreCase("asc");
        Pager<UserProductView> viewProduct;
        List<UserProductView> productViewList;

        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQueryUser = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQueryUser.from(Product.class);
        List<Predicate> predicate = new ArrayList<>();
        Join<Product, Category> categoryJoin = productRoot.join(CATEGORY, JoinType.INNER);
        Join<Product, Store> storeJoin = productRoot.join(STORE, JoinType.INNER);
        Join<Store, User> userJoin = storeJoin.join(USER, JoinType.INNER);

        Predicate predicateUserRole = criteriaBuilder.equal(userJoin.get(ROLE), User.Role.VENDOR.value);
        Predicate predicateUserStatus = criteriaBuilder.equal(userJoin.get(STATUS), User.Status.ACTIVE.value);
        Predicate predicateProductStatus = criteriaBuilder.equal(productRoot.get(STATUS),
                Product.Status.ACTIVE.value);

        predicate.add(predicateUserStatus);
        predicate.add(predicateUserRole);
        predicate.add(predicateProductStatus);

        if (!StringUtils.isEmpty(CategoryId)) {
            productUtil.idMatcher(CategoryId, CATEGORY_INVALID);
            String[] categoryId = CategoryId.split(SEPARATOR);
            Predicate predicateCategory = categoryJoin.get("id").in(categoryId);
            predicate.add(predicateCategory);
        }
        if (!StringUtils.isEmpty(StoreId)) {
            productUtil.idMatcher(StoreId, STORE_INVALID);
            String[] storeId = StoreId.split(SEPARATOR);
            Predicate predicateStore = storeJoin.get("id").in(storeId);
            predicate.add(predicateStore);
        }
        if (!StringUtils.isEmpty(searchData)) {
            searchData = searchData.trim();
            Predicate predicateProductName = criteriaBuilder.like(criteriaBuilder.lower(productRoot.get(NAME)),
                    "%" + searchData + "%");
            Predicate predicateCategoryName = criteriaBuilder
                    .like(criteriaBuilder.lower(productRoot.get(CATEGORY).get(NAME)), "%" + searchData + "%");
            Predicate predicateStoreName = criteriaBuilder
                    .like(criteriaBuilder.lower(productRoot.get(STORE).get(NAME)), "%" + searchData + "%");
            Predicate predicateSearch = criteriaBuilder.or(predicateProductName, predicateCategoryName,
                    predicateStoreName);

            predicate.add(predicateSearch);
        }

        Predicate whereCondition = criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));
        criteriaQueryUser.where(whereCondition);

        if (sort != null) {

            if (order.equalsIgnoreCase("asc")) {
                criteriaQueryUser.orderBy(criteriaBuilder.asc(productRoot.get(sort)));
            } else {
                criteriaQueryUser.orderBy(criteriaBuilder.desc(productRoot.get(sort)));
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText("item.invalid.sort", null, "en"));
        }
        TypedQuery<Product> list = entityManager.createQuery(criteriaQueryUser);
        list.setFirstResult((page - 1) * size);
        list.setMaxResults(size);
        Long count;
        count = (long) entityManager.createQuery(criteriaQueryUser).getResultList().size();
        Page<Product> result = new PageImpl<>(list.getResultList(),
                PageRequest.of(page - 1, size, (orderD == true) ? Sort.Direction.DESC : Sort.Direction.ASC, sort),
                count);
        productViewList = result.stream()
                .map(product -> {
                    Boolean isCarted = cartUtil.getProduct(product.getId());
                    Boolean isAvailable = dateValidation.productAvailabilityCheck(product, formattedStartDate,
                            formattedEndDate, quantity);
                    Wishlist wishlist = wishlistUtil.getWishListId(product.getId());
                    Boolean isWishlisted = Boolean.FALSE;
                    Integer wishlistId = 0;
                    if (wishlist != null) {
                        isWishlisted = true;
                        wishlistId = wishlist.getId();
                    }
                    return new UserProductView(product, isAvailable, isCarted, isWishlisted, wishlistId);
                })
                .collect(Collectors.toList());
        if (latitude != null && longitude != null &&!"price".equals(sort)) {

            getNearByStores = nearBySearchService.userProductDetailView(latitude, longitude, radius);
            List<Integer> nearbyStoreIds = getNearByStores.stream()
                    .map(Store::getId)
                    .collect(Collectors.toList());

            Comparator<UserProductView> storeIdComparator = Comparator.comparing(view -> {
                Integer storeId = view.getStoreId();
                return nearbyStoreIds.indexOf(storeId);
            });
            productViewList.sort(storeIdComparator);
        }

        viewProduct = new Pager<>(size, count.intValue(), page);
        viewProduct.setResult(productViewList);
        return viewProduct;

    }

    public ProductDetailView detailView(Integer productId) {

        try {

            productAcceptedStatus = new byte[] { Product.Status.ACTIVE.value, Product.Status.INACTIVE.value };

            Product products = productRepository.findByIdAndStatusIn(productId, productAcceptedStatus);

            imageProductAcceptedStatus = new byte[] { ImageProduct.Status.ACTIVE.value,
                    ImageProduct.Status.INACTIVE.value };

            ImageProduct image = imageProductRepository.findByProductIdAndStatusIn(productId,
                    imageProductAcceptedStatus);

            return new ProductDetailView(products, image);

        } catch (NullPointerException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"), e);

        }

    }

    @Override
    public Pager<UserProductView> vendorViewProduct(String searchData, Integer page, Integer size, String sort,
            String order,
            String CategoryId, String StoreId) {
        boolean orderD = !order.equalsIgnoreCase("asc");

        Pager<UserProductView> viewProduct;
        List<UserProductView> productViewList;

        try {
            page = Integer.parseInt(String.valueOf(page));
            size = Integer.parseInt(String.valueOf(size));
        } catch (NumberFormatException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
        }

        if (page < 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("Page.natual", null, "en"));
        }

        if (size < 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("Size.natural", null, "en"));
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQueryUser = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQueryUser.from(Product.class);
        List<Predicate> predicate = new ArrayList<>();
        Join<Product, Category> categoryJoin = productRoot.join(CATEGORY, JoinType.INNER);
        Join<Product, Store> storeJoin = productRoot.join(STORE, JoinType.INNER);
        Join<Store, User> userJoin = storeJoin.join(USER, JoinType.INNER);

        Predicate predicateUserId = criteriaBuilder.equal(userJoin.get(ID), SecurityUtil.getCurrentUserId());

        Predicate predicateUserRole = criteriaBuilder.equal(userJoin.get(ROLE), User.Role.VENDOR.value);
        Predicate predicateUserStatus = criteriaBuilder.equal(userJoin.get(STATUS), User.Status.ACTIVE.value);
        Predicate predicateProductStatus = criteriaBuilder.equal(productRoot.get(STATUS),
                Product.Status.ACTIVE.value);

        predicate.add(predicateUserId);
        predicate.add(predicateUserStatus);
        predicate.add(predicateUserRole);
        predicate.add(predicateProductStatus);

        if (!StringUtils.isEmpty(CategoryId)) {
            productUtil.idMatcher(CategoryId, CATEGORY_INVALID);
            String[] categoryId = CategoryId.split(SEPARATOR);
            Predicate predicateCategory = categoryJoin.get(ID).in(categoryId);
            predicate.add(predicateCategory);
        }
        if (!StringUtils.isEmpty(StoreId)) {
            productUtil.idMatcher(StoreId, STORE_INVALID);
            String[] storeId = StoreId.split(SEPARATOR);
            Predicate predicateStore = storeJoin.get(ID).in(storeId);
            predicate.add(predicateStore);
        }

        if (!StringUtils.isEmpty(searchData)) {

            searchData = searchData.trim();
            Predicate predicateProductName = criteriaBuilder.like(criteriaBuilder.lower(productRoot.get(NAME)),
                    "%" + searchData + "%");
            Predicate predicateCategoryName = criteriaBuilder
                    .like(criteriaBuilder.lower(productRoot.get(CATEGORY).get(NAME)), "%" + searchData + "%");
            Predicate predicateStoreName = criteriaBuilder
                    .like(criteriaBuilder.lower(productRoot.get(STORE).get(NAME)), "%" + searchData + "%");
            Predicate predicateSearch = criteriaBuilder.or(predicateProductName, predicateCategoryName,
                    predicateStoreName);

            predicate.add(predicateSearch);
        }

        Predicate whereCondition = criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));
        criteriaQueryUser.where(whereCondition);

        if (sort != null) {

            if (order.equalsIgnoreCase("asc")) {
                criteriaQueryUser.orderBy(criteriaBuilder.asc(productRoot.get(sort)));
            } else {
                criteriaQueryUser.orderBy(criteriaBuilder.desc(productRoot.get(sort)));
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText("item.invalid.sort", null, "en"));
        }
        TypedQuery<Product> list = entityManager.createQuery(criteriaQueryUser);
        list.setFirstResult((page - 1) * size);
        list.setMaxResults(size);
        Long count;
        count = (long) entityManager.createQuery(criteriaQueryUser).getResultList().size();
        Page<Product> result = new PageImpl<>(list.getResultList(),
                PageRequest.of(page - 1, size, (orderD == true) ? Sort.Direction.DESC : Sort.Direction.ASC, sort),
                count);

        productViewList = result.stream()
                .map(product -> new UserProductView(product))
                .collect(Collectors.toList());

        viewProduct = new Pager<>(size, count.intValue(), page);
        viewProduct.setResult(productViewList);
        return viewProduct;

    }

    @Override
    public UserProductDetailView userProductDetailView(Integer productId, String startDate, String endDate,
            Integer quantity) {
        Boolean isWishlisted;
        Boolean isAvailable;
        Integer wishlistId;
        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        Wishlist wishlist = wishlistRepository.findByProductIdAndUserIdAndStatusIn(productId, user, wishlistExists);
        if (wishlist != null) {
            isWishlisted = true;
            wishlistId = wishlist.getId();
        } else {
            isWishlisted = false;
            wishlistId = 0;
        }

        Date formattedStartDate = dateValidation.stringToDateFormat(startDate, INVALID_START_DATE);
        Date formattedEndDate = dateValidation.stringToDateFormat(endDate, INVALID_END_DATE);
        try {
            productAcceptedStatus = new byte[] { Product.Status.ACTIVE.value };
            Product products = productRepository.findByIdAndStatusIn(productId, productAcceptedStatus);
            imageProductAcceptedStatus = new byte[] { ImageProduct.Status.ACTIVE.value };
            ArrayList<ImageProduct> image = imageProductRepository.findByProductIdAndStatus(productId,
                    ImageProduct.Status.ACTIVE.value);
            List<UserProductImageView> imageView = new ArrayList<>();
            UserProductImageView userProductImageView = new UserProductImageView();
            for (ImageProduct imageProduct : image) {
                userProductImageView.setImage1(products.getCoverImage());
                userProductImageView.setImage2(imageProduct.getImage1());
                userProductImageView.setImage3(imageProduct.getImage2());
                userProductImageView.setImage4(imageProduct.getImage3());
                userProductImageView.setImage5(imageProduct.getImage4());
                imageView.add(userProductImageView);
            }
            isAvailable = dateValidation.productAvailabilityCheck(products, formattedStartDate, formattedEndDate,
                    quantity);
            Cart cart = cartRepository.findByProductIdAndStatusAndUserId(productId, Cart.Status.ACTIVE.value,
                    SecurityUtil.getCurrentUserId());

            Boolean isCarted;
            if (cart == null) {
                isCarted = Boolean.FALSE;
            } else {
                isCarted = Boolean.TRUE;
            }
            return new UserProductDetailView(products, isAvailable, isCarted, isWishlisted, wishlistId, imageView);
        } catch (NullPointerException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"), e);
        }
    }

    @Override
    public UserAvailabilityView availabilityCheck(Integer productId, String startDate, String endDate,
            Integer quantity) {
        Boolean isAvailable;
        int totalRemaining;

        dateValidation.verifyDateExists(startDate, INVALID_START_DATE);
        dateValidation.verifyDateExists(endDate, INVALID_END_DATE);
        Product product = productUtil.validateProduct(productId);
        Date formattedStartDate = dateValidation.stringToDateFormat(startDate, INVALID_START_DATE);
        Date formattedEndDate = dateValidation.stringToDateFormat(endDate, INVALID_END_DATE);
        totalRemaining = dateValidation.productAvailabilityCheckNoAuth(productId, formattedStartDate, formattedEndDate,
                quantity);
        isAvailable = dateValidation.productAvailabilityCheck(product, formattedStartDate, formattedEndDate, quantity);

        if (quantity < 1) {
            isAvailable = false;
            return new UserAvailabilityView(isAvailable, totalRemaining);
        }
        return new UserAvailabilityView(isAvailable, totalRemaining);
    }

    public ResponseEntity<String> updateProductImage(ImageProductForm form) {
        userUtil.validateVendorCheck(UNABLE_TO_PERFORM);

        final byte[] imageProductStatus = { ImageProduct.Status.ACTIVE.value };

        User user = userRepository.findByIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);

        if (user.getStatus() != User.Status.ACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));
        } else {
            ImageProduct image = imageProductRepository.findByProductIdAndStatusIn(form.getProductId(),
                    imageProductStatus);

            if (Objects.isNull(image)) {
                throw new BadRequestException(
                        languageUtil.getTranslatedText(INVALID_PRODUCT_ID, null, "en"));
            }
                image.setImage1(form.getImage1());
                productUtil.imageTwoExists(image, form);
                productUtil.imageThreeExists(image, form);
                productUtil.imageFourExists(image, form);
                imageProductRepository.save(image);
                return new ResponseEntity<>(HttpStatus.OK);

        }

    }

}
