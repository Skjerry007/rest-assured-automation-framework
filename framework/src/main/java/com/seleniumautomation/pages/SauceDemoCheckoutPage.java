package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.LocatorUtil;

public class SauceDemoCheckoutPage {
    private WebDriver driver;
    private static final String LOCATOR_FILE = "SauceDemoLocators";

    public SauceDemoCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUserInfo(String firstName, String lastName, String postalCode) {
        LoggerUtil.info("Entering checkout user info: {} {} {}", firstName, lastName, postalCode);
        By firstNameField = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "FIRST_NAME_FIELD"));
        By lastNameField = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "LAST_NAME_FIELD"));
        By postalCodeField = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "POSTAL_CODE_FIELD"));
        
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void continueCheckout() {
        LoggerUtil.info("Continuing checkout");
        By continueButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "CONTINUE_BUTTON"));
        driver.findElement(continueButton).click();
    }

    public void finishCheckout() {
        LoggerUtil.info("Finishing checkout");
        By finishButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "FINISH_BUTTON"));
        driver.findElement(finishButton).click();
    }

    public boolean isCheckoutComplete() {
        By successMessage = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "SUCCESS_MESSAGE"));
        boolean complete = !driver.findElements(successMessage).isEmpty();
        LoggerUtil.info("Checkout complete: {}", complete);
        return complete;
    }

    public boolean isErrorDisplayed() {
        By errorMessage = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "CHECKOUT_ERROR_MESSAGE"));
        return !driver.findElements(errorMessage).isEmpty();
    }
} 