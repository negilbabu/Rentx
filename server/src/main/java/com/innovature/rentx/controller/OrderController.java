package com.innovature.rentx.controller;

import com.innovature.rentx.form.OrderForm;
import com.innovature.rentx.form.OrderPreSummary;
import com.innovature.rentx.service.OrderService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.UserOrderDetailView;
import com.innovature.rentx.view.UserOrderListView;
import com.innovature.rentx.view.UserOrderSummaryPageView;
import com.innovature.rentx.view.VendorOrderListView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("user/order/add")
    public ResponseEntity<String> addOrder(@Valid @RequestBody OrderForm form) {
        return orderService.addOrder(form);
    }

    @PostMapping("user/order/summary")
    public UserOrderSummaryPageView cartList(@Valid @RequestBody OrderPreSummary form,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20000000", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "DESC") String order) {
        return orderService.orderSummeryList(form, page, limit, sort, order);
    }

    @GetMapping("user/order/history/detailedView/{orderProductId}")
    public UserOrderDetailView detailView(@PathVariable Integer orderProductId) {
        return orderService.detailView(orderProductId);
    }

    @GetMapping("vendor/order/history/detailedView/{orderProductId}")
    public UserOrderDetailView vendorOrdersDetailView(@PathVariable Integer orderProductId) {
        return orderService.detailView(orderProductId);
    }

    @PutMapping("user/order/cancel/{orderProductId}")
    public ResponseEntity<String> userCancelOrder(@PathVariable Integer orderProductId) {
        return orderService.userCancelOrder(orderProductId);
    }

    @PutMapping("vendor/order/cancel/{orderProductId}")
    public ResponseEntity<String> uvendorCancelOrder(@PathVariable Integer orderProductId) {
        return orderService.userCancelOrder(orderProductId);
    }

    @GetMapping("user/order/history")
    public Pager<UserOrderListView> userOrderListViewPager(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "7", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "DESC") String order,
            @RequestParam(name = "searchData", required = false) String searchDate) {
        return orderService.OrderListViewPager(page, limit, sort, order, searchDate);
    }

    @GetMapping("vendor/order/history")
    public Pager<VendorOrderListView> vendorOrderListViewPager(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "7", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "DESC") String order,
            @RequestParam(name = "searchData", required = false) String searchDate) {
        return orderService.vendorOrderListViewPager(page, limit, sort, order, searchDate);
    }
}