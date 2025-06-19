package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;

public class SauceDemoInventoryPage {
    private WebDriver driver;

    // Locators
    private By addToCartButton = By.cssSelector("button[data-test^='add-to-cart']");
    private By cartIcon = By.className("shopping_cart_link");

    public SauceDemoInventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addFirstItemToCart() {
        LoggerUtil.info("Adding first item to cart");
        driver.findElements(addToCartButton).get(0).click();
    }

    public void goToCart() {
        LoggerUtil.info("Navigating to cart");
        driver.findElement(cartIcon).click();
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