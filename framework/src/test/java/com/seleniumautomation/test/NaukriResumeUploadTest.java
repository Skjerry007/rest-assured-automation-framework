package com.seleniumautomation.test;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.utils.GmailService;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.LocatorUtil;
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

            // Use LocatorUtil to fetch locators from properties file
            String emailField = LocatorUtil.getLocator("NaukriLocators", "EMAIL_FIELD");
            String passwordField = LocatorUtil.getLocator("NaukriLocators", "PASSWORD_FIELD");
            String loginButton = LocatorUtil.getLocator("NaukriLocators", "LOGIN_BUTTON");
            String otpField = LocatorUtil.getLocator("NaukriLocators", "OTP_FIELD");
            String verifyOtpButton = LocatorUtil.getLocator("NaukriLocators", "VERIFY_OTP_BUTTON");
            String updateProfileButton = LocatorUtil.getLocator("NaukriLocators", "UPDATE_PROFILE_BUTTON");
            String uploadResumeButton = LocatorUtil.getLocator("NaukriLocators", "UPLOAD_RESUME_BUTTON");
            String uploadSuccessMessage = LocatorUtil.getLocator("NaukriLocators", "UPLOAD_SUCCESS_MESSAGE");

            // Use these locators in the test
            keywords.typeText(By.xpath(emailField), System.getProperty("naukri.email"));
            keywords.typeText(By.xpath(passwordField), System.getProperty("naukri.password"));
            keywords.clickElement(By.xpath(loginButton));
            String otp = GmailService.getOTPFromEmail("Naukri Login OTP");
            keywords.typeText(By.xpath(otpField), otp);
            keywords.clickElement(By.xpath(verifyOtpButton));
            keywords.clickElement(By.xpath(updateProfileButton));
            keywords.uploadFile(By.xpath(uploadResumeButton), RESUME_PATH);
            Assert.assertTrue(keywords.isElementDisplayed(By.xpath(uploadSuccessMessage)), "Resume upload failed");

            LoggerUtil.info("Resume uploaded successfully");

        } catch (Exception e) {
            LoggerUtil.error("Error during resume upload: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
