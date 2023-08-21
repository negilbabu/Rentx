package com.innovature.rentx.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.Repository;

import com.innovature.rentx.entity.OrderProduct;

public interface OrderRepository extends Repository<OrderProduct,Integer> {

    OrderProduct save(OrderProduct orderProduct);

    Collection <OrderProduct> findByStatusAndEndDate(byte status, Date endDate);

    Collection <OrderProduct> findByStatusAndEndDateLessThan(byte status, Date endDate);

    Optional <OrderProduct> findById(Integer orderProductId);

    Optional <OrderProduct> findByStatusAndId(byte value, Integer orderProductId);

    Optional <OrderProduct> findByStatusAndEndDateLessThanAndId(byte status, Date endDate, int orderId);

    Page<OrderProduct> findAllByOrderProductMasterIdInAndStatusInAndProductNameContainingIgnoreCase(List<Integer> orderMasterId, byte[] status, String productName, PageRequest of);
    int countByOrderProductMasterIdInAndStatusInAndProductNameContainingIgnoreCase(List<Integer> orderMasterId, byte[] status, String productName);

    List <OrderProduct>findAllByProductStoreUserId(Integer userId);

}
