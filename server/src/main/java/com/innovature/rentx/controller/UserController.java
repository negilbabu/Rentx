package com.innovature.rentx.controller;

import com.innovature.rentx.entity.User;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.GoogleSignInForm;
import com.innovature.rentx.form.ProfileEditForm;
import com.innovature.rentx.form.UserChangePasswordForm;
import com.innovature.rentx.form.UserForm;
import com.innovature.rentx.service.ProductService;
import com.innovature.rentx.service.UserService;
import com.innovature.rentx.util.Pager;
import com.innovature.rentx.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
//    EmailTokenView
    @PostMapping("/add")
    public TestViewOtp add(@Valid @RequestBody UserForm form) {
        return userService.add(form, User.Role.USER.value);
    }
    @PostMapping("/googleAuth")
    public UserView googleAuth(@Valid @RequestBody GoogleSignInForm form)
            throws BadRequestException, GeneralSecurityException, IOException {
        return userService.googleAuth(form, User.Role.USER.value);
    }
    @GetMapping("/product/list")
    public Pager<UserProductView> productList(
            @RequestParam(name = "search", defaultValue = "", required = false) String searchData,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "15", required = false) Integer limit,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "ASC") String order,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "store", required = false) String store,
            @RequestParam(name="startDate", required = false) String startDate,
            @RequestParam(name="endDate", required = false) String endDate,
            @RequestParam(name="quantity" ,defaultValue = "1", required = false) Integer quantity,
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name="longitude", required = false) Double longitude
            ) {


        String formattedStartDate;
        String formattedEndDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate == null) {
            Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            formattedStartDate = dateFormat.format(currentDate);
        } else {
            formattedStartDate = startDate;
        }
        if (endDate == null) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Date nextDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
            formattedEndDate = dateFormat.format(nextDate);
        } else {
            formattedEndDate = endDate;
        }
        return productService.viewProduct(searchData, page, limit, sort, order, category, store,formattedStartDate,formattedEndDate,quantity, latitude, longitude);
    }
    @GetMapping("/product/{productId}")
    public UserProductDetailView userProductView(@PathVariable Integer productId,
                                                 @RequestParam(name="startDate", required = false) String startDate,
                                                 @RequestParam(name="endDate", required = false) String endDate,
                                                 @RequestParam(name="quantity" ,defaultValue = "1", required = false) Integer quantity) {
        String formattedStartDate;
        String formattedEndDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate == null) {
            Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            formattedStartDate = dateFormat.format(currentDate);
        } else {
            formattedStartDate = startDate;
        }
        if (endDate == null) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Date nextDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
            formattedEndDate = dateFormat.format(nextDate);
        } else {
            formattedEndDate = endDate;
        }

        return productService.userProductDetailView(productId, formattedStartDate, formattedEndDate, quantity);
    }
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserChangePasswordForm form) {
        return userService.UserChangePassword(form);
    }

    @GetMapping("/profile/view")
    public AdminVendorView profileDetails() {
        return userService.userDetailView();
    }

    @PutMapping("profile/update")
    public ResponseEntity<String> profileEdit(@Valid @RequestBody ProfileEditForm form){
        return userService.updateProfile(form, User.Role.USER.value);
    }
}
