package com.innovature.rentx.service.impl;

import com.innovature.rentx.entity.User;
import com.innovature.rentx.entity.VendorDetails;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.form.BankDetailsForm;
import com.innovature.rentx.form.ProfileEditForm;
import com.innovature.rentx.form.VendorDetailsForm;
import com.innovature.rentx.repository.UserRepository;
import com.innovature.rentx.repository.VendorDetailsRepository;
import com.innovature.rentx.service.VendorService;
import com.innovature.rentx.util.LanguageUtil;
import com.innovature.rentx.util.UserUtil;
import com.innovature.rentx.view.VendorDetailsView;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.innovature.rentx.security.util.SecurityUtil;
import com.innovature.rentx.security.util.TokenGenerator;
import com.innovature.rentx.security.util.TokenGenerator.Status;

@Service
public class VendorServiceImpl implements VendorService {

    private static final String PURPOSE_VENDOR_REG_STAGE_2 = "PURPOSE_VENDOR_REG_STAGE_2";

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorDetailsRepository vendorDetailsRepository;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private VendorDetailsRepository vendorRepository;

    private static final String UNABLE_TO_PERFORM = "unable.to.perform.this.action";

    public VendorServiceImpl(UserRepository userRepository2, VendorDetailsRepository vendorDetailsRepository,
            LanguageUtil languageUtil2, TokenGenerator tokenGenerator2) {
    }

    @Override
    public VendorDetailsView add(VendorDetailsForm form) throws BadRequestException {

        Status status = userUtil.verifyPurposeToken(PURPOSE_VENDOR_REG_STAGE_2, form.getEmailToken());
        String email = status.data;
        User user = userRepository.findByEmailId(email);

        if (user.getStatus() == User.Status.STAGE1.value) {

            VendorDetails vendorDetails = new VendorDetails();
            vendorDetails.setAccountNumber(form.getAccountNumber());
            vendorDetails.setHolderName(form.getHolderName());
            vendorDetails.setIfsc(form.getIfsc());
            vendorDetails.setPan(form.getPan());
            vendorDetails.setGst(form.getGst());
            vendorDetails.setUserId(user.getId());
            Date dt = new Date();
            vendorDetails.setCreatedAt(dt);
            vendorDetails.setUpdatedAt(dt);

            vendorRepository.save(vendorDetails);
            user.setStatus(User.Status.STAGE2.value);
            userRepository.save(user);
            return new VendorDetailsView(vendorDetails);
        } else
            throw new BadRequestException(languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en"));

    }

    @Override
    public ResponseEntity<String> updateProfile(ProfileEditForm form) {

        User user = userUtil.validateVendor(UNABLE_TO_PERFORM);
        user.setUsername(form.getUsername());
        user.setPhone(form.getPhone());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateBankDetails(BankDetailsForm form) {

        userUtil.validateVendor(UNABLE_TO_PERFORM);
        VendorDetails vendorDetails = vendorDetailsRepository.findByUserId(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en")));
        vendorDetails.setAccountNumber(form.getAccountNumber());
        vendorDetails.setHolderName(form.getHolderName());
        vendorDetails.setIfsc(form.getIfsc());
        vendorDetails.setPan(form.getPan());
        vendorDetails.setGst(form.getGst());
        vendorDetailsRepository.save(vendorDetails);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    public VendorDetailsView bankDetailsView() {

        userUtil.validateVendorWithStatus0And7(UNABLE_TO_PERFORM);
        VendorDetails vendorDetails = vendorDetailsRepository.findByUserId(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText(UNABLE_TO_PERFORM, null, "en")));
        return new VendorDetailsView(vendorDetails);

    }

}
