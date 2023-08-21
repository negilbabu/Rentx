package com.innovature.rentx.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.innovature.rentx.form.AddressForm;
import com.innovature.rentx.view.AddressView;
import com.innovature.rentx.view.UserAddressView;

public interface AddressService {

    ResponseEntity<String> add(AddressForm form);

    Collection<UserAddressView> list();

    ResponseEntity<String> edit(Integer id, @Valid AddressForm form);

    ResponseEntity<String> delete(Integer id);

    AddressView viewAddressDetail(Integer addressId);
    
}
