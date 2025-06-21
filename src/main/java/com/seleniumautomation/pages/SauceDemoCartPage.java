package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import com.seleniumautomation.utils.LocatorUtil;
import org.openqa.selenium.WebElement;
import java.util.List;

public class SauceDemoCartPage {
    private WebDriver driver;
    private WaitUtil waitUtil;

    // Enhanced self-healing locators with multiple fallback strategies
    private By checkoutButton = LocatorUtil.selfHealing(
        By.id("checkout"),
        By.cssSelector("#checkout"),
        By.cssSelector("button[data-test='checkout']"),
        By.xpath("//button[@data-test='checkout']"),
        By.cssSelector("button.checkout"),
        By.xpath("//button[contains(@class,'checkout')]"),
        By.cssSelector("button[id*='checkout']"),
        By.xpath("//button[contains(@id,'checkout')]"),
        By.cssSelector("a[href*='checkout']"),
        By.xpath("//a[contains(@href,'checkout')]")
    );
    
    private By cartItem = LocatorUtil.selfHealing(
        By.className("cart_item"),
        By.cssSelector(".cart_item"),
        By.xpath("//div[contains(@class,'cart_item')]"),
        By.cssSelector("[data-test='cart-item']"),
        By.xpath("//*[@data-test='cart-item']"),
        By.cssSelector(".cart-list .cart_item"),
        By.xpath("//div[contains(@class,'cart-list')]//div[contains(@class,'cart_item')]")
    );
    
    private By cartList = LocatorUtil.selfHealing(
        By.className("cart_list"),
        By.cssSelector(".cart_list"),
        By.xpath("//div[contains(@class,'cart_list')]"),
        By.cssSelector("[data-test='cart-list']"),
        By.xpath("//*[@data-test='cart-list']"),
        By.cssSelector(".cart-list"),
        By.xpath("//div[contains(@class,'cart-list')]")
    );

    public SauceDemoCartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public boolean isCartItemPresent() {
        try {
            LoggerUtil.info("Checking if cart items are present");
            
            // First check if we're on the cart page
            try {
                WebElement cartListElement = driver.findElement(cartList);
                LoggerUtil.info("Cart list element found, confirming we're on cart page");
            } catch (Exception e) {
                LoggerUtil.error("Not on cart page - cart list element not found: {}", e.getMessage());
                return false;
            }
            
            // Wait for cart items to be present
            try {
                List<WebElement> cartItems = driver.findElements(cartItem);
                boolean present = !cartItems.isEmpty();
                LoggerUtil.info("Cart items found: {} items present", cartItems.size());
                return present;
            } catch (Exception e) {
                LoggerUtil.error("Error finding cart items: {}", e.getMessage());
                return false;
            }
        } catch (Exception e) {
            LoggerUtil.error("Error checking cart items: {}", e.getMessage());
            return false;
        }
    }

    public void proceedToCheckout() {
        LoggerUtil.info("Proceeding to checkout");
        try {
            WebElement checkoutElement = driver.findElement(checkoutButton);
            waitUtil.waitForElementToBeClickable(checkoutElement).click();
            LoggerUtil.info("Checkout button clicked successfully");
            
            // Wait for navigation to checkout page
            boolean navigated = false;
            for (int i = 0; i < 10; i++) {
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("/checkout-step-one.html")) {
                    navigated = true;
                    LoggerUtil.info("Successfully navigated to checkout page: {}", currentUrl);
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (!navigated) {
                LoggerUtil.warn("Navigation to checkout page may have failed. Current URL: {}", driver.getCurrentUrl());
            }
            
        } catch (Exception e) {
            LoggerUtil.error("Error proceeding to checkout: {}", e.getMessage());
            throw new RuntimeException("Failed to proceed to checkout", e);
        }
    }
    
    public int getCartItemCount() {
        try {
            List<WebElement> cartItems = driver.findElements(cartItem);
            int count = cartItems.size();
            LoggerUtil.info("Cart contains {} items", count);
            return count;
        } catch (Exception e) {
            LoggerUtil.error("Error getting cart item count: {}", e.getMessage());
            return 0;
        }
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
} 