package com.innovature.rentx.controller;

import com.innovature.rentx.service.WishlistService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.UserWishlistDetailView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<Integer> add(@PathVariable("productId") Integer productId) {
        return wishlistService.add(productId);
    }

    @PutMapping("/remove/{wishlistId}")
    public ResponseEntity<String> delete(@PathVariable("wishlistId") Integer wishlistId) {
        return wishlistService.delete(wishlistId);
    }

    @GetMapping("/list")
    public Pager<UserWishlistDetailView> userWishlistDetailViewPager(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "7", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "updatedAt") String sort,
            @RequestParam(name = "order", defaultValue = "DESC") String order) {
        return wishlistService.listWishlistProduct(page, limit, sort, order);
    }

}
