package com.innovature.rentx.view;

import com.innovature.rentx.entity.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class AdminPaymentMethodView {

    private final Integer id;
    private final String name;
    private final byte status;

    private final Date createdAt;
    private final Date updatedAt;

    public AdminPaymentMethodView(PaymentMethod paymentMethod){
        this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
        this.status = paymentMethod.getStatus();
        this.createdAt = paymentMethod.getCreatedAt();
        this.updatedAt = paymentMethod.getUpdatedAt();
    }
}
