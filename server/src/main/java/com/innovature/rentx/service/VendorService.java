package com.innovature.rentx.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.innovature.rentx.form.BankDetailsForm;
import com.innovature.rentx.form.ProfileEditForm;
import com.innovature.rentx.form.VendorDetailsForm;

import com.innovature.rentx.view.VendorDetailsView;

public interface VendorService {

	VendorDetailsView add(@Valid VendorDetailsForm form);

    ResponseEntity<String> updateProfile(@Valid ProfileEditForm form);

    ResponseEntity<String> updateBankDetails(@Valid BankDetailsForm form);

    VendorDetailsView bankDetailsView();


}
