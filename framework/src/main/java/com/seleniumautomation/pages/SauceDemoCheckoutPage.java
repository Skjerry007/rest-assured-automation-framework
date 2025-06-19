package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;

public class SauceDemoCheckoutPage {
    private WebDriver driver;

    // Locators
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By successMessage = By.className("complete-header");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    public SauceDemoCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUserInfo(String firstName, String lastName, String postalCode) {
        LoggerUtil.info("Entering checkout user info: {} {} {}", firstName, lastName, postalCode);
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void continueCheckout() {
        LoggerUtil.info("Continuing checkout");
        driver.findElement(continueButton).click();
    }

    public void finishCheckout() {
        LoggerUtil.info("Finishing checkout");
        driver.findElement(finishButton).click();
    }

    public boolean isCheckoutComplete() {
        boolean complete = !driver.findElements(successMessage).isEmpty();
        LoggerUtil.info("Checkout complete: {}", complete);
        return complete;
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }
} 