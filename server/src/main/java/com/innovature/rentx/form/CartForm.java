package com.innovature.rentx.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartForm {

    @NotNull(message = "{product.id.required}")
    private Integer productId;

    @NotNull(message = "{quantity.should.required}")
    private Integer quantity;

    @NotNull(message = "{startDate.should.required}")
    private String startDate;

    @NotNull(message = "{endDate.should.required}")
    private String endDate;

}
