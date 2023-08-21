package com.innovature.rentx.form;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class CartFormTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testGetterAndSetter() {
        CartForm cartForm = new CartForm();

        cartForm.setProductId(1);
        cartForm.setQuantity(10);
        cartForm.setStartDate("2023-06-01");
        cartForm.setEndDate("2023-06-30");

        Assertions.assertEquals(1, cartForm.getProductId());
        Assertions.assertEquals(10, cartForm.getQuantity());
        Assertions.assertEquals("2023-06-01", cartForm.getStartDate());
        Assertions.assertEquals("2023-06-30", cartForm.getEndDate());
    }

    @Test
    void testProductIdNotNull() {
        CartForm cartForm = new CartForm();
        cartForm.setQuantity(10);
        cartForm.setStartDate("2023-06-01");
        cartForm.setEndDate("2023-06-30");

        Set<ConstraintViolation<CartForm>> violations = validator.validate(cartForm);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<CartForm> violation = violations.iterator().next();
        Assertions.assertEquals("{product.id.required}", violation.getMessage());
    }

    @Test
    void testQuantityNotNull() {
        CartForm cartForm = new CartForm();
        cartForm.setProductId(1);
        cartForm.setStartDate("2023-06-01");
        cartForm.setEndDate("2023-06-30");

        Set<ConstraintViolation<CartForm>> violations = validator.validate(cartForm);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<CartForm> violation = violations.iterator().next();
        Assertions.assertEquals("{quantity.should.required}", violation.getMessage());
    }

    @Test
    void testStartDateNotNull() {
        CartForm cartForm = new CartForm();
        cartForm.setProductId(1);
        cartForm.setQuantity(10);
        cartForm.setEndDate("2023-06-30");

        Set<ConstraintViolation<CartForm>> violations = validator.validate(cartForm);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<CartForm> violation = violations.iterator().next();
        Assertions.assertEquals("{startDate.should.required}", violation.getMessage());
    }

    @Test
    void testEndDateNotNull() {
        CartForm cartForm = new CartForm();
        cartForm.setProductId(1);
        cartForm.setQuantity(10);
        cartForm.setStartDate("2023-06-01");

        Set<ConstraintViolation<CartForm>> violations = validator.validate(cartForm);

        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<CartForm> violation = violations.iterator().next();
        Assertions.assertEquals("{endDate.should.required}", violation.getMessage());
    }

    @Test
    void testValidationSuccess() {
        CartForm cartForm = new CartForm();
        cartForm.setProductId(1);
        cartForm.setQuantity(10);
        cartForm.setStartDate("2023-06-01");
        cartForm.setEndDate("2023-06-30");

        Set<ConstraintViolation<CartForm>> violations = validator.validate(cartForm);

        Assertions.assertEquals(0, violations.size());
    }
}
