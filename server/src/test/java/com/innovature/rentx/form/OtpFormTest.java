package com.innovature.rentx.form;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OtpFormTest {


    @Test
    void testConstructor() {
        OtpForm actualOtpForm = new OtpForm();
        actualOtpForm.setEmailToken("ABC123");
        actualOtpForm.setOtp("Otp");
        assertEquals("ABC123", actualOtpForm.getEmailToken());
        assertEquals("Otp", actualOtpForm.getOtp());
    }

    @Test
    void testSetAndGetOtp() {
        OtpForm otpForm = new OtpForm();
        String otp = "123456";

        otpForm.setOtp(otp);
        String retrievedOtp = otpForm.getOtp();

        Assertions.assertEquals(otp, retrievedOtp);
    }

    @Test
    void testSetAndGetEmailToken() {
        OtpForm otpForm = new OtpForm();
        String emailToken = "abc123";

        otpForm.setEmailToken(emailToken);
        String retrievedEmailToken = otpForm.getEmailToken();

        Assertions.assertEquals(emailToken, retrievedEmailToken);
    }

    @Test
    void testOtpValidation() {
        OtpForm otpForm = new OtpForm();
        String validOtp = "123456";
        String invalidOtp = "123";

        otpForm.setOtp(validOtp);
        Assertions.assertDoesNotThrow(() -> {
        });

        otpForm.setOtp(invalidOtp);
        Assertions.assertThrows(ValidationException.class, () -> {
        });
    }

    @Test
    void testEmailTokenValidation() {
        OtpForm otpForm = new OtpForm();
        String validEmailToken = "abc123";
        String invalidEmailToken = "";

        otpForm.setEmailToken(validEmailToken);
        Assertions.assertDoesNotThrow(() -> {
        });

        otpForm.setEmailToken(invalidEmailToken);
        Assertions.assertThrows(ValidationException.class, () -> {
        });
    }
}

