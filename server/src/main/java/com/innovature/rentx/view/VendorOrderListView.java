package com.innovature.rentx.view;

import com.innovature.rentx.entity.OrderProduct;
import com.innovature.rentx.entity.OrderProductMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class VendorOrderListView {

    private Integer orderId;
    private String productName;
    private Date startDate;
    private Date endDate;
    private byte status;
    private Integer productQuantity;
    private double price;
    private double totalPrice;
    private String coverImage;
    private String thumbnail;

    private String paymentMethod;
    private String orderedAddress;
    public VendorOrderListView(OrderProduct orderProduct, OrderProductMaster orderProductMaster){
        this.orderId = orderProduct.getId();
        this.productName = orderProduct.getProduct().getName();
        this.startDate = orderProduct.getStartDate();
        this.endDate = orderProduct.getEndDate();
        this.status = orderProduct.getStatus();
        this.productQuantity = orderProduct.getQuantity();
        this.price = orderProduct.getProduct().getPrice();
        this.totalPrice = orderProduct.getTotalPrice();
        this.coverImage = orderProduct.getProduct().getCoverImage();
        this.thumbnail= orderProduct.getProduct().getThumbnail();

        this.paymentMethod=orderProductMaster.getPaymentMethod().getName();
        this.orderedAddress=orderProductMaster.getAddress().getName();
    }
}
