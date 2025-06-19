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

    // Self-healing locators
    private By itemName = LocatorUtil.selfHealing(
        By.cssSelector(".inventory_item_name"),
        By.xpath("//div[contains(@class,'inventory_item_name')]")
    );
    private By itemDesc = LocatorUtil.selfHealing(
        By.cssSelector(".inventory_item_desc"),
        By.xpath("//div[contains(@class,'inventory_item_desc')]")
    );
    private By itemPrice = LocatorUtil.selfHealing(
        By.cssSelector(".inventory_item_price"),
        By.xpath("//div[contains(@class,'inventory_item_price')]")
    );
    private By finishButton = By.id("finish");
    private By paymentInfo = By.xpath("//div[contains(text(),'Payment Information')]/following-sibling::div");
    private By shippingInfo = By.xpath("//div[contains(text(),'Shipping Information')]/following-sibling::div");
    private By itemTotal = By.xpath("//div[contains(text(),'Item total')]");
    private By tax = By.xpath("//div[contains(text(),'Tax')]");
    private By total = By.xpath("//div[contains(text(),'Total')]");

    public SauceDemoCheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public String getItemName() {
        WebElement el = driver.findElement(itemName);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getItemDescription() {
        WebElement el = driver.findElement(itemDesc);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getItemPrice() {
        WebElement el = driver.findElement(itemPrice);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getPaymentInfo() {
        WebElement el = driver.findElement(paymentInfo);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getShippingInfo() {
        WebElement el = driver.findElement(shippingInfo);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getItemTotal() {
        WebElement el = driver.findElement(itemTotal);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getTax() {
        WebElement el = driver.findElement(tax);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getTotal() {
        WebElement el = driver.findElement(total);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public void clickFinish() {
        WebElement el = driver.findElement(finishButton);
        waitUtil.waitForElementToBeClickable(el).click();
    }
}
