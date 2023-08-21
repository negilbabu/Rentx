package com.innovature.rentx.form;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VendorDetailsFormTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testVendorDetailsFormValidation() {
        VendorDetailsForm form = new VendorDetailsForm();
        form.setAccountNumber("12345678");
        form.setHolderName("JohnDoe");
        form.setIfsc("ABCD1234567");
        form.setGst("GST1234567890");
        form.setPan("PAN1234567");
        form.setEmailToken("abc123");

        Set<ConstraintViolation<VendorDetailsForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testVendorDetailsFormValidationWithInvalidData() {
        VendorDetailsForm form = new VendorDetailsForm();
        form.setAccountNumber("1234567a");
        form.setHolderName("John Doe123");
        form.setIfsc("ABCD12345!");
        form.setGst("GST1234567890123");
        form.setPan("PAN12345678$");
        form.setEmailToken("");

        Set<ConstraintViolation<VendorDetailsForm>> violations = validator.validate(form);

        assertEquals(8, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("holderName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ifsc")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gst")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("pan")));
    }
}
