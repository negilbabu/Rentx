package com.innovature.rentx.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class OrderPreSummary {

    @NotNull
    private Set<@NotNull Integer> cartId;
}
