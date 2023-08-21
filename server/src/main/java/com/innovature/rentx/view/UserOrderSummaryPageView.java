package com.innovature.rentx.view;

import com.innovature.rentx.util.Pager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserOrderSummaryPageView {

    Pager<UserOrderSummeryListProduct>orderSummeryListProductPager;
    private Double totalPrice;
    private Double priceIncludeVat;

    public UserOrderSummaryPageView(Pager<UserOrderSummeryListProduct>orderSummeryListProductPager,Double totalPrice,Double priceIncludeVat){
        this.orderSummeryListProductPager=orderSummeryListProductPager;
        this.totalPrice=totalPrice;
        this.priceIncludeVat=priceIncludeVat;
    }
}
