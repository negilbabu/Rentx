package com.innovature.rentx.service;

import com.innovature.rentx.form.PaymentAddForm;
import com.innovature.rentx.view.AdminPaymentMethodView;
import com.innovature.rentx.view.VendorPaymentListView;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public interface PaymentMethodService {
    ResponseEntity<String>addPaymentMethod(PaymentAddForm form);

    ResponseEntity<String>editPaymentMethod(Integer id,PaymentAddForm form);
    Collection<AdminPaymentMethodView>adminListPaymentMethods();

    ResponseEntity<String>deletePaymentMethod(Integer id);

    List<VendorPaymentListView> viewPaymentMethodsList();
    AdminPaymentMethodView paymentDetailsView(Integer id);
}
