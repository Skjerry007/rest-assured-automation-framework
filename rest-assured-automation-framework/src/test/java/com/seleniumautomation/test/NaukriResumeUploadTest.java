package com.seleniumautomation.tests;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.locators.NaukriLocators;
import com.seleniumautomation.utils.GmailService;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NaukriResumeUploadTest extends BaseTest {

    private static final String RESUME_PATH = "/usr/uploads/resume.pdf";
    private static final String LOGIN_URL = "https://www.naukri.com/nlogin/login";

    @Test(description = "Test resume upload on Naukri with OTP verification")
    public void testResumeUpload() {
        SeleniumKeywords keywords = new SeleniumKeywords();

        try {
            keywords.navigateToUrl(LOGIN_URL);

            // Type email
            keywords.typeText(By.xpath(NaukriLocators.EMAIL_FIELD), 
                System.getProperty("naukri.email"));

            // Get OTP
            String otp = GmailService.getOTPFromEmail("Naukri Login OTP");

            // Enter OTP
            keywords.typeText(By.xpath(NaukriLocators.OTP_FIELD), otp);
            keywords.clickElement(By.xpath(NaukriLocators.VERIFY_OTP_BUTTON));

            // Upload Resume
            keywords.clickElement(By.xpath(NaukriLocators.UPDATE_PROFILE_BUTTON));
            keywords.uploadFile(By.xpath(NaukriLocators.UPLOAD_RESUME_BUTTON), RESUME_PATH);

            // Assert upload success
            Assert.assertTrue(
                keywords.isElementDisplayed(By.xpath(NaukriLocators.UPLOAD_SUCCESS_MESSAGE)),
                "Resume upload failed"
            );

            LoggerUtil.info("Resume uploaded successfully");

        } catch (Exception e) {
            LoggerUtil.error("Error during resume upload: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
