package com.innovature.rentx.form;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangePasswordFormTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testChangePasswordFormValidation() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setPassword("Negil@1999");
        form.setEmailToken("abc123");

        Set<ConstraintViolation<ChangePasswordForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testChangePasswordFormValidationWithInvalidData() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setPassword("weak");
        form.setEmailToken("");

        Set<ConstraintViolation<ChangePasswordForm>> violations = validator.validate(form);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("emailToken")));
    }
}
