package com.innovature.rentx.form;

import com.innovature.rentx.form.validaton.Password;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordForm {

    private String oldPassword;

    @Password
    private String newPassword;

    }
