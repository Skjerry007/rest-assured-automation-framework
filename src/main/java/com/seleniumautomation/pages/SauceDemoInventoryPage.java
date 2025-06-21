package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import com.seleniumautomation.utils.LocatorUtil;

public class SauceDemoInventoryPage {
    private WebDriver driver;
    private WaitUtil waitUtil;

    // Enhanced self-healing locators with multiple fallback strategies
    private By addToCartButton = LocatorUtil.selfHealing(
        By.cssSelector("button[data-test^='add-to-cart']"),
        By.cssSelector("button[data-test*='add-to-cart']"),
        By.xpath("//button[contains(@data-test,'add-to-cart')]"),
        By.cssSelector("button.add-to-cart"),
        By.xpath("//button[contains(@class,'add-to-cart')]"),
        By.cssSelector("button[id*='add']"),
        By.xpath("//button[contains(@id,'add')]")
    );
    
    private By cartIcon = LocatorUtil.selfHealing(
        By.className("shopping_cart_link"),
        By.cssSelector(".shopping_cart_link"),
        By.xpath("//a[contains(@class,'shopping_cart_link')]"),
        By.cssSelector("a[href*='cart']"),
        By.xpath("//a[contains(@href,'cart')]"),
        By.cssSelector("[data-test='shopping-cart-link']"),
        By.xpath("//*[@data-test='shopping-cart-link']"),
        By.cssSelector("a.cart"),
        By.xpath("//a[contains(@class,'cart')]")
    );

    public SauceDemoInventoryPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public void addFirstItemToCart() {
        LoggerUtil.info("Adding first item to cart");
        try {
            List<WebElement> addButtons = driver.findElements(addToCartButton);
            if (!addButtons.isEmpty()) {
                WebElement addButton = addButtons.get(0);
                waitUtil.waitForElementToBeClickable(addButton).click();
                LoggerUtil.info("Successfully added first item to cart");
                
                // Wait a moment for the cart state to update
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                LoggerUtil.error("No add to cart buttons found");
                throw new RuntimeException("No add to cart buttons found");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error adding item to cart: {}", e.getMessage());
            throw new RuntimeException("Failed to add item to cart", e);
        }
    }

    public void goToCart() {
        LoggerUtil.info("Navigating to cart");
        try {
            WebElement cartElement = driver.findElement(cartIcon);
            waitUtil.waitForElementToBeClickable(cartElement).click();
            LoggerUtil.info("Cart icon clicked successfully");
            
            // Wait for the cart page URL with enhanced waiting
            boolean navigated = false;
            for (int i = 0; i < 15; i++) {
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("/cart.html")) {
                    navigated = true;
                    LoggerUtil.info("Successfully navigated to cart page: {}", currentUrl);
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (!navigated) {
                LoggerUtil.warn("Navigation to cart page may have failed. Current URL: {}", driver.getCurrentUrl());
                // Try to navigate directly
                try {
                    driver.get(driver.getCurrentUrl().replace("/inventory.html", "/cart.html"));
                    LoggerUtil.info("Attempted direct navigation to cart page");
                } catch (Exception e) {
                    LoggerUtil.error("Direct navigation also failed: {}", e.getMessage());
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.error("Error navigating to cart: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate to cart", e);
        }
    }

    public String getFirstItemName() {
        try {
            By itemNameLocator = LocatorUtil.selfHealing(
                By.className("inventory_item_name"),
                By.cssSelector(".inventory_item_name"),
                By.xpath("//div[contains(@class,'inventory_item_name')]"),
                By.cssSelector("[data-test='inventory-item-name']"),
                By.xpath("//*[@data-test='inventory-item-name']")
            );
            
            List<WebElement> items = driver.findElements(itemNameLocator);
            if (!items.isEmpty()) {
                return items.get(0).getText();
            } else {
                LoggerUtil.error("No inventory items found");
                return null;
            }
        } catch (Exception e) {
            LoggerUtil.error("Error getting first item name: {}", e.getMessage());
            return null;
        }
    }
} 