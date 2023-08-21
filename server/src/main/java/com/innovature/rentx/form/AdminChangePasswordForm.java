package com.innovature.rentx.form;

import com.innovature.rentx.form.validaton.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class AdminChangePasswordForm {

    @NotNull(message = "password.required")
    private String oldPassword;

    @Password
    private String newPassword;
}
