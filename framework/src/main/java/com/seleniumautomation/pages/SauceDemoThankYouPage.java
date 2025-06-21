package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.seleniumautomation.utils.WaitUtil;
import com.seleniumautomation.utils.LocatorUtil;

public class SauceDemoThankYouPage {
    private WebDriver driver;
    private WaitUtil waitUtil;
    private static final String LOCATOR_FILE = "SauceDemoLocators";

    public SauceDemoThankYouPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public String getThankYouHeader() {
        By thankYouHeader = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "THANK_YOU_HEADER"));
        WebElement el = driver.findElement(thankYouHeader);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getThankYouMessage() {
        By thankYouMessage = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "THANK_YOU_MESSAGE"));
        WebElement el = driver.findElement(thankYouMessage);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public void clickBackHome() {
        By backHomeButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "BACK_HOME_BUTTON"));
        WebElement el = driver.findElement(backHomeButton);
        waitUtil.waitForElementToBeClickable(el).click();
    }
    
    public boolean isCheckoutComplete() {
        try {
            By thankYouHeader = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "THANK_YOU_HEADER"));
            WebElement el = driver.findElement(thankYouHeader);
            String headerText = waitUtil.waitForElementToBeVisible(el).getText();
            return "THANK YOU FOR YOUR ORDER".equals(headerText);
        } catch (Exception e) {
            return false;
        }
    }
}
