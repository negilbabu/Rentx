package com.innovature.rentx.controller;

import com.innovature.rentx.form.*;
import com.innovature.rentx.service.AdminService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // admin registration
    @PostMapping("/add")
    public ResponseEntity<String>add(@Valid @RequestBody AdminForm form){
        return adminService.add(form);
    }
    // admin login
    @PostMapping("/login")
    public AdminLoginView login(@Valid @RequestBody LoginForm form){
        return adminService.login(form);
    }

    // admin forgot password
    @PostMapping("/forgetPassword")
    public TestViewOtp forgetEmail(@Valid @RequestBody EmailForm form){
        return adminService.forgetPasswordEmail(form);
    }
    // admin refresh token generation
    @PutMapping("/login")
    public RefreshTokenView refresh(@RequestBody RefreshTokenForm form){
        return adminService.refresh(form);
    }

    // admin forget password OTP verification
    @PostMapping("otp/verify/forgetpassword")
    public EmailTokenView forgetVerify(@Valid @RequestBody OtpForm form) {
        return adminService.forgetVerify(form);
    }

    // admin change password
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordForm form){
        return adminService.changePassword(form);
    }

    // admin vendor approve reject
    @PutMapping("/vendor/approve/{id}")
    public ResponseEntity<String> approveReject( @PathVariable("id") Integer id ,@RequestParam Integer btnValue){
        return adminService.approveReject(id, btnValue);
    }
    // admin vendor List
    @GetMapping("/vendor/list")
    public Pager<AdminVendorView>vendorList(@RequestParam(name ="searchData",defaultValue="" ,required = false) String searchData,
                                            @RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                                            @RequestParam(name = "size",defaultValue = "7",required = false)Integer limit,
                                            @RequestParam(name = "sort",defaultValue = "updatedAt") String sort,
                                            @RequestParam(name = "order",defaultValue = "DESC") String order,
                                            @RequestParam(defaultValue = "1") Integer statusValue){
        return adminService.listVendor(searchData,page,limit,sort,order,statusValue);
    }

    //admin user List
    @GetMapping("/user/list")
    public Pager<AdminVendorView>userList(@RequestParam(name ="search",defaultValue="" ,required = false) String searchData,
                                          @RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                                          @RequestParam(name = "size",defaultValue = "7",required = false)Integer limit,
                                          @RequestParam(name = "sort",defaultValue = "updatedAt") String sort,
                                          @RequestParam(name = "order",defaultValue = "DESC") String order){
        return adminService.listUser(searchData,page,limit,sort,order);
    }

    //admin user block/ unblock
    @PutMapping("/user/manageUser/{id}")
    public ResponseEntity<String> userBlockUnblock( @PathVariable("id") Integer id ,@RequestParam Integer btnValue){
        return adminService.UserBlockUnblock(id, btnValue);
    }

    @PutMapping("/profile/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody AdminChangePasswordForm form) {
        return adminService.adminChangePassword(form);
    }
}