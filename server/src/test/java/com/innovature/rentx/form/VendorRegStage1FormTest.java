package com.innovature.rentx.form;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VendorRegStage1FormTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testVendorRegStage1FormValidation() {
        VendorRegStage1Form form = new VendorRegStage1Form();
        form.setUsername("john123");
        form.setPhone("1234567890");
        form.setEmailToken("abc123");

        Set<ConstraintViolation<VendorRegStage1Form>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testVendorRegStage1FormValidationWithInvalidData() {
        VendorRegStage1Form form = new VendorRegStage1Form();
        form.setUsername("john!");
        form.setPhone("12345a");
        form.setEmailToken("");

        Set<ConstraintViolation<VendorRegStage1Form>> violations = validator.validate(form);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("emailToken")));
    }
}
