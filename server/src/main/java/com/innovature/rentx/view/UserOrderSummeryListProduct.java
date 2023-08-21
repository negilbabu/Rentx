package com.innovature.rentx.view;

import com.innovature.rentx.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderSummeryListProduct {

    private Integer id;
    private Integer quantity;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isAvailable;
    private Integer productId;
    private String productName;
    private double price;
    private String coverImage;
    private String thumbnail;
    private String categoryName;
    private Integer productQuantity;
    private String storeName;
    private int dateDifference;
    private double priceIncludedDate;

    public UserOrderSummeryListProduct(Cart cart, boolean isAvailable,int dateDifference,double priceIncludedDate) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.startDate = cart.getStartDate();
        this.endDate = cart.getEndDate();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getUpdatedAt();
        this.productId=cart.getProduct().getId();
        this.productName = cart.getProduct().getName();
        this.productQuantity = cart.getProduct().getAvailableStock();
        this.price = cart.getProduct().getPrice();
        this.coverImage = cart.getProduct().getCoverImage();
        this.thumbnail = cart.getProduct().getThumbnail();
        this.categoryName = cart.getProduct().getCategory().getName();
        this.storeName = cart.getProduct().getStore().getName();
        this.isAvailable = isAvailable;
        this.dateDifference=dateDifference;
        this.priceIncludedDate=priceIncludedDate;

    }

}
