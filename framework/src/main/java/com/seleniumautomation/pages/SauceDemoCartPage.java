package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import org.openqa.selenium.WebElement;

public class SauceDemoCartPage {
    private WebDriver driver;
    private WaitUtil waitUtil;

    // Locators
    private By checkoutButton = By.id("checkout");
    private By cartItem = By.className("cart_item");

    public SauceDemoCartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil();
    }

    public boolean isCartItemPresent() {
        try {
            // Check if we're on the cart page by looking for cart-specific elements
            try {
                driver.findElement(By.className("cart_list"));
            } catch (Exception e) {
                LoggerUtil.error("Not on cart page: {}", e.getMessage());
                return false;
            }
            
            // Wait for cart items to be present
            try {
                waitUtil.waitForElementToBeVisible(driver.findElement(cartItem));
                boolean present = !driver.findElements(cartItem).isEmpty();
                LoggerUtil.info("Cart item present: {}", present);
                return present;
            } catch (Exception e) {
                LoggerUtil.error("No cart items found: {}", e.getMessage());
                return false;
            }
        } catch (Exception e) {
            LoggerUtil.error("Error checking cart items: {}", e.getMessage());
            return false;
        }
    }

    public void proceedToCheckout() {
        LoggerUtil.info("Proceeding to checkout");
        WebElement checkoutElement = driver.findElement(checkoutButton);
        waitUtil.waitForElementToBeClickable(checkoutElement).click();
    }
} 