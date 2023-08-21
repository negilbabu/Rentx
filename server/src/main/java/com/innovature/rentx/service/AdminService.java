package com.innovature.rentx.service;

import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.*;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

public interface AdminService {

    RefreshTokenView refresh(RefreshTokenForm form) throws BadRequestException;

    ResponseEntity<String> add(AdminForm form);

    AdminLoginView login(LoginForm form) throws BadRequestException;

    TestViewOtp forgetPasswordEmail(EmailForm form) throws BadRequestException;

    EmailTokenView forgetVerify(@Valid OtpForm form);

    ResponseEntity<String> changePassword(@Valid ChangePasswordForm form);

    ResponseEntity<String> approveReject(Integer id, Integer btnValue);

    Pager<AdminVendorView> listVendor(String searchData, Integer page, Integer limit, String sort, String order,
            Integer statusValue);

    Pager<AdminVendorView> listUser(String searchData, Integer page, Integer limit, String sort, String order);

    ResponseEntity<String> UserBlockUnblock(Integer id, Integer btnValue);

    ResponseEntity<String> adminChangePassword(@Valid AdminChangePasswordForm form);

}
