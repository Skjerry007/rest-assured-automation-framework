package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import com.seleniumautomation.utils.LocatorUtil;
import java.util.List;

public class SauceDemoInventoryPage {
    private WebDriver driver;
    private WaitUtil waitUtil;
    private static final String LOCATOR_FILE = "SauceDemoLocators";

    public SauceDemoInventoryPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public void addFirstItemToCart() {
        LoggerUtil.info("Adding first item to cart");
        By addToCartButton = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "ADD_TO_CART_BUTTON"));
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
        By cartIcon = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "CART_ICON"));
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
        By itemNameLocator = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "INVENTORY_ITEM_NAME"));
        return driver.findElements(itemNameLocator).get(0).getText();
    }

    public String getFirstItemDescription() {
        By itemDescLocator = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "INVENTORY_ITEM_DESC"));
        return driver.findElements(itemDescLocator).get(0).getText();
    }

    public String getFirstItemPrice() {
        By itemPriceLocator = LocatorUtil.getByLocator(LocatorUtil.getLocator(LOCATOR_FILE, "INVENTORY_ITEM_PRICE"));
        return driver.findElements(itemPriceLocator).get(0).getText();
    }
}