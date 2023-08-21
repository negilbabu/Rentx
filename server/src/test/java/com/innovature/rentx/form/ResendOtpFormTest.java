package com.innovature.rentx.form;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ResendOtpFormTest {

    @Test
    public void testResendOtpForm() {
        ResendOtpForm form = new ResendOtpForm();
        form.setEmailToken("1234567890");

        Assertions.assertTrue(form.isValid());

        Assertions.assertEquals("1234567890", form.getEmailToken());
    }

    @Test
    public void testResendOtpFormWithInvalidEmailToken() {
        ResendOtpForm form = new ResendOtpForm();
        form.setEmailToken("123456789");

        Assertions.assertFalse(form.isValid());
    }

    @Test
    public void testResendOtpFormWithEmptyEmailToken() {
        ResendOtpForm form = new ResendOtpForm();
        form.setEmailToken("");

        Assertions.assertFalse(form.isValid());
    }
}
