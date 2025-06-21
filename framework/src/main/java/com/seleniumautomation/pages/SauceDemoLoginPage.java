package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;

public class SauceDemoLoginPage {
    private WebDriver driver;
    private static final String LOCATOR_FILE = "SauceDemoLocators";

    public SauceDemoLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        LoggerUtil.info("Entering username: {}", username);
        By usernameField = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "USERNAME_FIELD"));
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        LoggerUtil.info("Entering password: {}", password);
        By passwordField = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "PASSWORD_FIELD"));
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        LoggerUtil.info("Clicking login button");
        By loginButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "LOGIN_BUTTON"));
        driver.findElement(loginButton).click();
    }

    public boolean isErrorDisplayed() {
        By errorMessage = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ERROR_MESSAGE"));
        return !driver.findElements(errorMessage).isEmpty();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
} 