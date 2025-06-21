package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;

public class SauceDemoInventoryPage {
    private WebDriver driver;
    private WaitUtil waitUtil;

    // Locators
    private By addToCartButton = By.cssSelector("button[data-test^='add-to-cart']");
    private By cartIcon = By.className("shopping_cart_link");

    public SauceDemoInventoryPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public void addFirstItemToCart() {
        LoggerUtil.info("Adding first item to cart");
        WebElement addButton = driver.findElements(addToCartButton).get(0);
        waitUtil.waitForElementToBeClickable(addButton).click();
        
        // Wait a moment for the cart state to update
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void goToCart() {
        LoggerUtil.info("Navigating to cart");
        WebElement cartElement = driver.findElement(cartIcon);
        waitUtil.waitForElementToBeClickable(cartElement).click();
        // Wait for the cart page URL
        for (int i = 0; i < 10; i++) {
            if (driver.getCurrentUrl().contains("/cart.html")) {
                break;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        LoggerUtil.info("Current URL after navigating to cart: {}", driver.getCurrentUrl());
    }

    public String getFirstItemName() {
        return driver.findElements(By.className("inventory_item_name")).get(0).getText();
    }

    public String getFirstItemDescription() {
        return driver.findElements(By.className("inventory_item_desc")).get(0).getText();
    }

    public String getFirstItemPrice() {
        return driver.findElements(By.className("inventory_item_price")).get(0).getText();
    }
}