package com.innovature.rentx.repository;

import com.innovature.rentx.entity.OrderProductMaster;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface OrderMasterRepository extends Repository<OrderProductMaster,Integer> {

    OrderProductMaster save(OrderProductMaster orderProductMaster);

    Optional <OrderProductMaster> findById(Integer id);

    List<OrderProductMaster>findByUserIdAndStatusIn(Integer userId,byte[] orderMasterStatus);
}
