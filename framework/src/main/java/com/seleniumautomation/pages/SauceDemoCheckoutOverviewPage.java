package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;

public class SauceDemoCheckoutOverviewPage {
    private WebDriver driver;
    private WaitUtil waitUtil;
    private static final String LOCATOR_FILE = "SauceDemoLocators";

    public SauceDemoCheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public String getItemName() {
        By itemName = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ITEM_NAME"));
        WebElement el = driver.findElement(itemName);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getItemDescription() {
        By itemDesc = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ITEM_DESC"));
        WebElement el = driver.findElement(itemDesc);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getItemPrice() {
        By itemPrice = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ITEM_PRICE"));
        WebElement el = driver.findElement(itemPrice);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getPaymentInfo() {
        By paymentInfo = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "PAYMENT_INFO"));
        WebElement el = driver.findElement(paymentInfo);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getShippingInfo() {
        By shippingInfo = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "SHIPPING_INFO"));
        WebElement el = driver.findElement(shippingInfo);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getItemTotal() {
        By itemTotal = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ITEM_TOTAL"));
        WebElement el = driver.findElement(itemTotal);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getTax() {
        By tax = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "TAX"));
        WebElement el = driver.findElement(tax);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public String getTotal() {
        By total = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "TOTAL"));
        WebElement el = driver.findElement(total);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    
    public void clickFinish() {
        By finishButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "OVERVIEW_FINISH_BUTTON"));
        WebElement el = driver.findElement(finishButton);
        waitUtil.waitForElementToBeClickable(el).click();
    }
    
    public void finishCheckout() {
        LoggerUtil.info("Finishing checkout");
        clickFinish();
    }
}
