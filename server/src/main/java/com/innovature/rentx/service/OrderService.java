package com.innovature.rentx.service;

import com.innovature.rentx.form.OrderForm;
import com.innovature.rentx.form.OrderPreSummary;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.UserOrderDetailView;
import com.innovature.rentx.view.UserOrderListView;
import com.innovature.rentx.view.UserOrderSummaryPageView;
import com.innovature.rentx.view.VendorOrderListView;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<String> addOrder(OrderForm form);

    UserOrderSummaryPageView orderSummeryList(OrderPreSummary form, Integer page, Integer limit, String sort,
            String order);

    void restore();

    UserOrderDetailView detailView(Integer orderProductId);

    ResponseEntity<String> userCancelOrder(Integer orderProductId);

    Pager<UserOrderListView> OrderListViewPager(Integer page, Integer limit, String sort, String order,
            String searchData);

    Pager<VendorOrderListView> vendorOrderListViewPager(Integer page, Integer limit, String sort, String order,
            String searchData);

}
