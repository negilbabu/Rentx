package com.innovature.rentx.service;

import com.innovature.rentx.form.CartForm;
import com.innovature.rentx.view.UserCartListView;
import com.innovature.rentx.view.UserCartView;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

public interface CartService {

    UserCartView save(@Valid CartForm form);

    UserCartView modifyCart(Integer cartId, Integer quantity, String startDate, String endDate);

    ResponseEntity<String> removeItem(Integer cartId);

    UserCartListView viewCartProduct(Integer page, Integer limit, String sort, String order);

}
