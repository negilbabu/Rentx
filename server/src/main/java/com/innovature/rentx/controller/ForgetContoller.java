package com.innovature.rentx.controller;

import com.innovature.rentx.form.ChangePasswordForm;
import com.innovature.rentx.form.EmailForm;
import com.innovature.rentx.form.OtpForm;
import com.innovature.rentx.service.UserService;
import com.innovature.rentx.view.EmailTokenView;
import com.innovature.rentx.view.TestViewOtp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ForgetContoller {

    private final UserService userService;

    //user forgot password email
    @PostMapping("forgetPassword")
    public TestViewOtp forgetEmail(@Valid @RequestBody EmailForm form){
        return userService.forgetPasswordEmail(form);
    }

    //user forgot password otp verification
    @PostMapping("/otp/verify/forgetpassword")
    public EmailTokenView forgetVerify(@Valid @RequestBody OtpForm form) {
        return userService.forgetVerify(form);
    }

    //user changed password
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordForm form){
        return userService.changePassword(form);
    }
}