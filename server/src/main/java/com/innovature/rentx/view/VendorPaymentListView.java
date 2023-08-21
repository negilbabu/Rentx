package com.innovature.rentx.view;

import com.innovature.rentx.entity.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor(force = true)
public class VendorPaymentListView {
    private final Integer id;
    private final String name;


    public VendorPaymentListView(PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
    }

    public VendorPaymentListView(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
