package com.innovature.rentx.controller;

import com.innovature.rentx.form.LoginForm;
import com.innovature.rentx.form.RefreshTokenForm;
import com.innovature.rentx.service.UserService;
import com.innovature.rentx.view.LoginView;
import com.innovature.rentx.view.RefreshTokenView;
import com.innovature.rentx.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    //user login
    @PostMapping
    public LoginView login(@Valid @RequestBody LoginForm form){
        return userService.login(form);
    }

    //user refresh token generation
    @PutMapping
    public RefreshTokenView refresh(@RequestBody RefreshTokenForm form){
        return userService.refresh(form);
    }

    @GetMapping("/currentUser")
    public UserView currentUser() {
        return userService.currentUser();
    }
}