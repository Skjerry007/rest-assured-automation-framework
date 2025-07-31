package com.seleniumautomation.test;

import com.seleniumautomation.utils.GmailService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OTPTest {

    @Test
    public void testFetchOtpFromGmail() {
        try {
            String subject = "Your verification code"; // update if needed
            String otp = GmailService.getOTPFromEmail(subject);
            System.out.println("Retrieved OTP: " + otp);
            Assert.assertNotNull(otp, "OTP should not be null");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to retrieve OTP: " + e.getMessage());
        }
    }
}
