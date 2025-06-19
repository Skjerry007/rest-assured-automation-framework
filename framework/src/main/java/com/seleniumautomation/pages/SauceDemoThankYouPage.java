package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.seleniumautomation.utils.WaitUtil;

public class SauceDemoThankYouPage {
    private WebDriver driver;
    private WaitUtil waitUtil;
    private By thankYouHeader = By.className("complete-header");
    private By thankYouMessage = By.xpath("//div[@class='complete-text']");
    private By backHomeButton = By.id("back-to-products");

    public SauceDemoThankYouPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public String getThankYouHeader() {
        WebElement el = driver.findElement(thankYouHeader);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public String getThankYouMessage() {
        WebElement el = driver.findElement(thankYouMessage);
        return waitUtil.waitForElementToBeVisible(el).getText();
    }
    public void clickBackHome() {
        WebElement el = driver.findElement(backHomeButton);
        waitUtil.waitForElementToBeClickable(el).click();
    }
}
