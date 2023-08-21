package com.innovature.rentx.repository;

import com.innovature.rentx.entity.PaymentMethod;
import com.innovature.rentx.view.VendorPaymentListView;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends Repository<PaymentMethod,Integer> {


    PaymentMethod save(PaymentMethod paymentMethod);
    Optional<PaymentMethod>findByNameAndStatus(String paymentName, byte status);
    PaymentMethod findByIdAndStatus(Integer paymentId, byte status);
    int countByStatus(byte status);

    Collection<PaymentMethod>findByStatusIn(byte[] status);
    List<VendorPaymentListView> findAllByStatus(byte status);
}
