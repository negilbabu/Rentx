package com.innovature.rentx.entity;

import org.junit.jupiter.api.Assertions;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Date;

class OtpTest {

    @Test
    void testOtp() {
        Otp otp = new Otp();

        LocalTime expiry = LocalTime.now();
        otp.setExpiry(expiry);
        Assertions.assertEquals(expiry, otp.getExpiry());

        otp.setEmail("test@gmail.com");
        otp.setOtp("123456");
        otp.setStatus(Otp.STATUS.ACTIVE.value);
        otp.setCreatedAt(new Date());

        assertEquals("test@gmail.com", otp.getEmail());
        assertEquals("123456", otp.getOtp());
        assertEquals(Otp.STATUS.ACTIVE.value, otp.getStatus());
        assertEquals(new Date(), otp.getCreatedAt());
    }

}
