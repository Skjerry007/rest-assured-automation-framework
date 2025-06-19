package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;

public class SauceDemoLoginPage {
    private WebDriver driver;

    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    public SauceDemoLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        LoggerUtil.info("Entering username: {}", username);
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        LoggerUtil.info("Entering password: {}", password);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        LoggerUtil.info("Clicking login button");
        driver.findElement(loginButton).click();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
} 