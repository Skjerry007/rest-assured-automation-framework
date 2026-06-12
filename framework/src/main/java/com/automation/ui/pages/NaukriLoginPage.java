package com.automation.ui.pages;

import com.automation.ui.base.BasePage;
import com.automation.ui.locators.NaukriLocators;
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
        typeText(emailField, email);
    }

    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    public void clickLogin() {
        clickElement(loginButton);
    }

    public void enterOtp(String otp) {
        typeText(otpField, otp);
    }

    public void clickVerifyOtp() {
        clickElement(verifyOtpButton);
    }
}
