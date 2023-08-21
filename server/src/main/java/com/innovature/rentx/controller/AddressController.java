package com.innovature.rentx.controller;

import java.security.Principal;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovature.rentx.form.AddressForm;
import com.innovature.rentx.service.AddressService;
import com.innovature.rentx.view.AddressView;
import com.innovature.rentx.view.UserAddressView;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("add")
    public ResponseEntity<String> add(@Valid @RequestBody AddressForm form) {
        return addressService.add(form);
    }

    @GetMapping("list")
    public Collection<UserAddressView> viewAddress(Principal p) {
        return addressService.list();
    }
    @GetMapping("list/{addressId}")
    public AddressView viewAddressDetail(Principal p, @PathVariable Integer addressId) {
        return addressService.viewAddressDetail(addressId);
    }

    @PutMapping("{addressId}")
    public ResponseEntity<String> edit(
    @PathVariable Integer addressId,@Valid @RequestBody AddressForm form ){
        return addressService.edit(addressId,form);
    }

    @PutMapping("remove/{addressId}")
    public ResponseEntity<String> remove(
    @PathVariable Integer addressId ){
        return addressService.delete(addressId);
    }

    
}
