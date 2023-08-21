package com.innovature.rentx.form;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PaymentAddForm {

    @NotBlank(message = "{payment.type.required}")
    @Size(min = 3,max = 20,message = "{payment.size.required}")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$",message = "{invalid.payment.type}")
    private String type;

}
