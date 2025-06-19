package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.restautomation.utils.LoggerUtil;

public class SauceDemoCartPage {
    private WebDriver driver;

    // Locators
    private By checkoutButton = By.id("checkout");
    private By cartItem = By.className("cart_item");

    public SauceDemoCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCartItemPresent() {
        boolean present = !driver.findElements(cartItem).isEmpty();
        LoggerUtil.info("Cart item present: {}", present);
        return present;
    }

    public void proceedToCheckout() {
        LoggerUtil.info("Proceeding to checkout");
        driver.findElement(checkoutButton).click();
    }
} 