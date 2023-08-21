    package com.innovature.rentx.view;

    import java.util.Date;

    import com.innovature.rentx.entity.OrderProduct;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Getter
    @NoArgsConstructor
    public class UserOrderListView {

        private Integer orderId;
        private String productName;
        private Date startDate;
        private Date endDate;
        private Integer noOfDays;
        private byte status;
        private Integer productQuantity;
        private double price;
        private double totalPrice;
        private String coverImage;
        private String thumbnail;

        public UserOrderListView(OrderProduct orderProduct,Integer noOfDays){
            this.orderId = orderProduct.getId();
            this.productName = orderProduct.getProduct().getName();
            this.startDate = orderProduct.getStartDate();
            this.endDate = orderProduct.getEndDate();
            this.noOfDays = noOfDays;
            this.status = orderProduct.getStatus();
            this.productQuantity = orderProduct.getQuantity();
            this.price = orderProduct.getProduct().getPrice();
            this.totalPrice = orderProduct.getTotalPrice();
            this.coverImage = orderProduct.getProduct().getCoverImage();
            this.thumbnail= orderProduct.getProduct().getThumbnail();
        }
    }

