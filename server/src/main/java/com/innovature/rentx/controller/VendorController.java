package com.innovature.rentx.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;

import javax.validation.Valid;
import com.innovature.rentx.form.*;
import com.innovature.rentx.service.ProductService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.innovature.rentx.entity.User;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.service.UserService;
import com.innovature.rentx.service.VendorService;

@RestController
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {

    @Autowired
    protected UserService userService;

    private final VendorService vendorService;
    private final ProductService productService;
    @PostMapping("/googleAuth")
    public UserView googleAuth(@Valid @RequestBody GoogleSignInForm form)
            throws BadRequestException, GeneralSecurityException, IOException {
        return userService.googleAuth(form, User.Role.VENDOR.value);
    }
    // initial reg
    @PostMapping("/registration")
    public TestViewOtp add(@Valid @RequestBody UserForm form) {
        return userService.add(form, User.Role.VENDOR.value);
    }
    // OTP verify
    @PostMapping("/otp/verify")
    public VendorOtpView verifyVendorOtp(@Valid @RequestBody OtpForm form) {
        return userService.verifyVendorOtp(form);
    }
    // stage1
    @PutMapping("/registration")
    public VendorRegStage1View vendorRegStage1(@Valid @RequestBody VendorRegStage1Form form) {
        return userService.vendorRegStage1(form);
    }
    // stage2
    @PostMapping("/bankDetails")
    public VendorDetailsView add(@Valid @RequestBody VendorDetailsForm form) {
        return vendorService.add(form);
    }
   
    @PostMapping("product/add")
    public ProductView createProduct(@Valid @RequestBody ProductForm form) {
        return productService.createProduct(form);
    }
    @PostMapping("product/addImage")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ImageProductForm form) {
        return productService.addImageProduct(form);
    }
    @PutMapping("product/delete/{productId}")
    public  ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
    return productService.deleteProduct(productId);
    }
    @PutMapping("product/update/{productId}")
    public  ResponseEntity<String> updateProduct(
        @Valid @RequestBody ProductForm form,    
    @PathVariable Integer productId) {
    return productService.updateProduct(productId, form);
    }
    @PutMapping("product/updateImage")
    public  ResponseEntity<String> updateProductImage(
            @Valid @RequestBody ImageProductForm form) {
        return productService.updateProductImage(form);
    }
    @GetMapping("product/detailView/{productId}")
    public  ProductDetailView detailView(@PathVariable Integer productId) {
    return productService.detailView(productId);
    }
    @GetMapping("/product/list")
    public Pager<UserProductView> productList(@RequestParam(name ="search",defaultValue="" ,required = false) String searchData,
                                              @RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                                              @RequestParam(name = "size",defaultValue = "7",required = false)Integer limit,
                                              @RequestParam(name = "sort",defaultValue = "name") String sort,
                                              @RequestParam(name = "order",defaultValue = "ASC") String order,
                                              @RequestParam(name="category",required = false) String category,
                                              @RequestParam(name="store",required = false) String store){
        return productService.vendorViewProduct(searchData,page,limit,sort,order,category,store);
    }

    //change password
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserChangePasswordForm form) {
        return userService.UserChangePassword(form);
    }
    @GetMapping("/profileDetails")
    public AdminVendorView profileDetails() {
        return userService.userDetailView();
    }

       @PutMapping("profile/update")
    public ResponseEntity<String> profileEdit(@Valid @RequestBody ProfileEditForm form){
        return vendorService.updateProfile(form);
    }

    @PutMapping("/bankDetails")
    public  ResponseEntity<String> updateBankDetails(@Valid @RequestBody BankDetailsForm form) {
        return vendorService.updateBankDetails(form);
    }

    @GetMapping("bankDetails/view")
    public VendorDetailsView bankDetails(Principal p){
        return vendorService.bankDetailsView();
    }
}