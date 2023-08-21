package com.innovature.rentx.view;

import java.util.Date;

import com.innovature.rentx.entity.OrderProduct;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserOrderDetailView {

    private Integer orderProductId;
    private String userName;
    private String paymentType;
    private String categoryName;
    private String subCategoryName;
    private String productName;
    private Date startDate;
    private Date endDate;
    private Integer noOfDays;
    private byte status;
    private Integer productQuantity;
    private double price;
    private double totalPrice;
 
    private String name;
    private String houseName;
    private String pinCode;
    private String city;
    private String state;
    private String phone;
    private byte type;
    private String coverImage;
    private String thumbnail;
    private  boolean isCancellable;
    private  double payableAmount;

    public UserOrderDetailView(OrderProduct orderProduct, Integer noOfDays,boolean isCancellable,double payableAmount){

        this.orderProductId = orderProduct.getId();
        this.userName = orderProduct.getOrderProductMaster().getUser().getUsername();
        this.paymentType = orderProduct.getOrderProductMaster().getPaymentMethod().getName();
        this.categoryName =orderProduct.getProduct().getCategory().getName();
        this.subCategoryName = orderProduct.getProduct().getSubCategory().getName();
        this.productName = orderProduct.getProduct().getName();
        this.startDate = orderProduct.getStartDate();
        this.endDate = orderProduct.getEndDate();
        this.noOfDays = noOfDays;
        this.status = orderProduct.getStatus();
        this.productQuantity = orderProduct.getQuantity();
        this.price = orderProduct.getProduct().getPrice();
        this.totalPrice = orderProduct.getTotalPrice();

        this.name = orderProduct.getOrderProductMaster().getAddress().getName();
        this.houseName = orderProduct.getOrderProductMaster().getAddress().getHouseName();
        this.pinCode = orderProduct.getOrderProductMaster().getAddress().getPinCode();
        this.city = orderProduct.getOrderProductMaster().getAddress().getCity();
        this.state = orderProduct.getOrderProductMaster().getAddress().getState();
        this.phone = orderProduct.getOrderProductMaster().getAddress().getPhone();
        this.type = orderProduct.getOrderProductMaster().getAddress().getType();

        this.coverImage = orderProduct.getProduct().getCoverImage();
        this.thumbnail= orderProduct.getProduct().getThumbnail();
        this.isCancellable = isCancellable;
        this.payableAmount = payableAmount;



    }
    
}

