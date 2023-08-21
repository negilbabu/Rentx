package com.innovature.rentx.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class OrderForm {

    @NotNull
    private Set<@NotNull Integer> cartId;
    @NotNull(message = "{address.id.required}")
    private Integer addressId;
    @NotNull(message = "{payment.type.required}")
    private Integer paymentId;

}
