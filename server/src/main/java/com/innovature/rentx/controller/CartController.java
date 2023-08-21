package com.innovature.rentx.controller;

import com.innovature.rentx.form.CartForm;
import com.innovature.rentx.service.CartService;
import com.innovature.rentx.view.UserCartListView;
import com.innovature.rentx.view.UserCartView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //user add item to cart
    @PostMapping("user/cart/add")
    public UserCartView cartAdd(@Valid @RequestBody CartForm form) {
        return cartService.save(form);
    }

    //user cart list items
    @GetMapping("user/cart/list")
    public UserCartListView cartList(@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "7", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "DESC") String order) {
        return cartService.viewCartProduct(page, limit, sort, order);
    }

    //user cart modify
    @PutMapping("user/cart/{cartId}")
    public UserCartView cartModify(
            @PathVariable Integer cartId,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate) {
        return cartService.modifyCart(cartId, quantity, startDate, endDate);
    }

    //user cart item remove
    @PutMapping("user/cart/remove/{cartId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Integer cartId) {
        return cartService.removeItem(cartId);
    }
}