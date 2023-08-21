package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.Product;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.entity.Wishlist;
import com.innovature.rentx.repository.WishlistRepository;
import com.innovature.rentx.service.WishlistService;
import com.innovature.rentx.util.*;
import com.innovature.rentx.view.UserWishlistDetailView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final ProductUtil productUtil;
    private final UserUtil userUtil;
    private final WishlistUtil wishlistUtil;
    private final CategoryUtil categoryUtil;
    private final WishlistRepository wishlistRepository;
    private final DateValidation dateValidation;

    private static final String UNABLE_PERFORM = "unable.to.perform.this.action";
    private static final String PAGE_NUMBER = "error.page.number.natural";

    private static final String SIZE_NUMBER = "error.page.size.natural";
    private static final String SORT_INVALID = "sort.invalid";
    private static final String DIRECTION_INVALID = "direction.invalid";
    private static final String INVALID_START_DATE = "start.date.validation";
    private static final String INVALID_END_DATE = "end.date.validation";

    @Override
    public ResponseEntity<Integer> add(Integer productId) {
        Integer wislistId = null;

        User user = userUtil.validateUser(UNABLE_PERFORM);
        Product product = productUtil.validateProduct(productId);
        Wishlist wishlistDataExist = wishlistUtil.checkWishlistExists(productId);
        if (wishlistDataExist == null) {
            Wishlist wishlist = new Wishlist();
            wishlist.setUserId(user);
            wishlist.setProduct(product);
            wishlist.setStatus(Wishlist.STATUS.ACTIVE.value);
            wishlistRepository.save(wishlist);
            wislistId= wishlist.getId();

        } else {
            wislistId = wishlistDataExist.getId();
            wishlistDataExist.setStatus(Wishlist.STATUS.ACTIVE.value);
            wishlistRepository.save(wishlistDataExist);

        }
        return new ResponseEntity<Integer>(wislistId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        userUtil.validateUser(UNABLE_PERFORM);
        Wishlist wishlist = wishlistUtil.wishlistExists(id);
        wishlist.setStatus(Wishlist.STATUS.DELETED.value);
        wishlistRepository.save(wishlist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Pager<UserWishlistDetailView> listWishlistProduct(Integer page, Integer size, String sort, String order) {

        String formattedStartDate;
        String formattedEndDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        formattedStartDate = dateFormat.format(currentDate);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Date nextDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
        formattedEndDate = dateFormat.format(nextDate);
        dateValidation.verifyDateExists(formattedStartDate, INVALID_START_DATE);
        dateValidation.verifyDateExists(formattedEndDate, INVALID_END_DATE);
        Date formattedStartDateCheck = dateValidation.stringToDateFormat(formattedStartDate, INVALID_START_DATE);
        Date formattedEndDateCheck = dateValidation.stringToDateFormat(formattedEndDate, INVALID_END_DATE);
        boolean orderD = !order.equalsIgnoreCase("asc");

        categoryUtil.checkListParams(page, size, sort, Sort.Direction.fromString(order.toUpperCase()), PAGE_NUMBER,
                SIZE_NUMBER, SORT_INVALID, DIRECTION_INVALID);

        Pageable pageable = PageRequest.of(page - 1, size, (orderD) ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        User user = userUtil.validateUser(UNABLE_PERFORM);

        Page<Wishlist> wishlistPage = wishlistRepository.findByUserIdAndStatusAndProductStatus(user, Wishlist.STATUS.ACTIVE.value,Product.Status.ACTIVE.value,
                pageable);
        List<Wishlist> result = wishlistPage.getContent();
        List<UserWishlistDetailView> userWishlistDetailViewList = result.stream()
                .map(wishlist -> {
                    Boolean isAvailable = dateValidation.productAvailabilityCheck(
                            wishlist.getProduct(),
                            formattedStartDateCheck,
                            formattedEndDateCheck,
                            1);
                    return new UserWishlistDetailView(wishlist, isAvailable);
                })
                .collect(Collectors.toList());

        int count = wishlistRepository.countByUserIdAndStatusAndProductStatus(user, Wishlist.STATUS.ACTIVE.value,Product.Status.ACTIVE.value);
        Pager<UserWishlistDetailView> pager = new Pager<>(size, count, page);
        pager.setResult(userWishlistDetailViewList);
        return pager;
    }

}
