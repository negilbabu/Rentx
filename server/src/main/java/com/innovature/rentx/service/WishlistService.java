package com.innovature.rentx.service;

import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.UserWishlistDetailView;
import org.springframework.http.ResponseEntity;

public interface WishlistService {

    ResponseEntity<Integer> add(Integer productId);
    ResponseEntity<String> delete(Integer id);

    Pager<UserWishlistDetailView> listWishlistProduct(Integer page, Integer limit, String sort, String order);
}
