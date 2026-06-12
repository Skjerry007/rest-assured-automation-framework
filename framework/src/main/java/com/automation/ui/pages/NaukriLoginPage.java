package com.automation.ui.pages;

import com.automation.ui.base.BasePage;
import com.automation.ui.locators.NaukriLocators;
import com.automation.common.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NaukriLoginPage extends BasePage {
    private final By emailField = NaukriLocators.Login.emailField();
    private final By passwordField = NaukriLocators.Login.passwordField();
    private final By loginButton = NaukriLocators.Login.loginButton();
    private final By otpField = NaukriLocators.Login.otpField();
    private final By verifyOtpButton = NaukriLocators.Login.verifyOtpButton();

    public NaukriLoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterEmail(String email) {
        try {
            LoggerUtil.info("Entering email in login page: {}", email);
            typeText(emailField, email);
            LoggerUtil.info("Successfully entered email");
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter email: {}", e.getMessage());
            throw new RuntimeException("Failed to enter email in login page", e);
        }
    }

    public void enterPassword(String password) {
        try {
            LoggerUtil.info("Entering password in login page");
            typeText(passwordField, password);
            LoggerUtil.info("Successfully entered password");
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter password: {}", e.getMessage());
            throw new RuntimeException("Failed to enter password in login page", e);
        }
    }

    public void clickLogin() {
        try {
            LoggerUtil.info("Clicking login button");
            clickElement(loginButton);
            LoggerUtil.info("Successfully clicked login button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to click login button: {}", e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    public void enterOtp(String otp) {
        try {
            LoggerUtil.info("Entering OTP: {}", otp);
            typeText(otpField, otp);
            LoggerUtil.info("Successfully entered OTP");
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter OTP: {}", e.getMessage());
            throw new RuntimeException("Failed to enter OTP", e);
        }
    }

    public void clickVerifyOtp() {
        try {
            LoggerUtil.info("Clicking verify OTP button");
            clickElement(verifyOtpButton);
            LoggerUtil.info("Successfully clicked verify OTP button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to click verify OTP button: {}", e.getMessage());
            throw new RuntimeException("Failed to click verify OTP button", e);
        }
    }
}
