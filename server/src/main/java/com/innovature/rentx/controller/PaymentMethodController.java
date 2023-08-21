package com.innovature.rentx.controller;

import com.innovature.rentx.form.PaymentAddForm;
import com.innovature.rentx.service.PaymentMethodService;
import com.innovature.rentx.view.AdminPaymentMethodView;
import com.innovature.rentx.view.VendorPaymentListView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/admin/paymentMethod/add")
    public ResponseEntity<String>addPaymentMethod(@Valid @RequestBody PaymentAddForm form){
        return paymentMethodService.addPaymentMethod(form);
    }

    @PutMapping("/admin/paymentMethod/edit/{paymentId}")
    public ResponseEntity<String>editPaymentMethod(@PathVariable Integer paymentId,
    @Valid @RequestBody PaymentAddForm form){
        return paymentMethodService.editPaymentMethod(paymentId, form);
    }


    @GetMapping("/admin/paymentMethod/list")
    public Collection<AdminPaymentMethodView> listPaymentMethods(){
        return paymentMethodService.adminListPaymentMethods();
    }
    @GetMapping("/user/paymentMethod/list")
    public List<VendorPaymentListView> vendorPayment(){
        return paymentMethodService.viewPaymentMethodsList();
    }

    @PutMapping("/admin/paymentMethod/{paymentId}")
    public ResponseEntity<String>deletePaymentMethod(@PathVariable Integer paymentId){
        return paymentMethodService.deletePaymentMethod(paymentId);
    }

    @GetMapping("/admin/paymentMethod/detail/{paymentId}")
    public AdminPaymentMethodView payementMethodDetails(@PathVariable Integer paymentId){
        return paymentMethodService.paymentDetailsView(paymentId);
    }
}
