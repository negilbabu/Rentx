package com.innovature.rentx.view;


import com.innovature.rentx.entity.Wishlist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserWishlistDetailView {

    private Integer id;
    private Integer productId;
    private String productName;
    private double price;
    private String coverImage;
    private String thumbnail;
    private String categoryName;
    private Integer productQuantity;
    private String storeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isAvailable;


    public UserWishlistDetailView(Wishlist wishlist,Boolean isAvailable){
        this.id = wishlist.getId();
        this.createdAt = wishlist.getCreatedAt();
        this.updatedAt = wishlist.getUpdatedAt();
        this.productId=wishlist.getProduct().getId();
        this.productName = wishlist.getProduct().getName();
        this.productQuantity = wishlist.getProduct().getAvailableStock();
        this.price = wishlist.getProduct().getPrice();
        this.coverImage = wishlist.getProduct().getCoverImage();
        this.thumbnail = wishlist.getProduct().getThumbnail();
        this.categoryName = wishlist.getProduct().getCategory().getName();
        this.storeName = wishlist.getProduct().getStore().getName();
        this.isAvailable=isAvailable;
    }

}
