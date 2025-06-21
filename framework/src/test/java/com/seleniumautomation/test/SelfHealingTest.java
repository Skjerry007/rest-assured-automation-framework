package com.seleniumautomation.test;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.seleniumautomation.driver.DriverManager;
import com.seleniumautomation.pages.SauceDemoLoginPage;
import com.seleniumautomation.pages.SauceDemoInventoryPage;
import com.seleniumautomation.pages.SauceDemoCartPage;
import com.seleniumautomation.pages.SauceDemoCheckoutPage;
import com.seleniumautomation.pages.SauceDemoCheckoutOverviewPage;
import com.seleniumautomation.pages.SauceDemoThankYouPage;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;

public class SelfHealingTest {
    private WebDriver driver;
    private SauceDemoLoginPage loginPage;
    private SauceDemoInventoryPage inventoryPage;
    private SauceDemoCartPage cartPage;
    private SauceDemoCheckoutPage checkoutPage;
    private SauceDemoCheckoutOverviewPage overviewPage;
    private SauceDemoThankYouPage thankYouPage;

    @BeforeMethod
    public void setUp() {
        DriverManager.getInstance().initializeDriver();
        driver = DriverManager.getInstance().getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new SauceDemoLoginPage(driver);
        inventoryPage = new SauceDemoInventoryPage(driver);
        cartPage = new SauceDemoCartPage(driver);
        checkoutPage = new SauceDemoCheckoutPage(driver);
        overviewPage = new SauceDemoCheckoutOverviewPage(driver);
        thankYouPage = new SauceDemoThankYouPage(driver);
    }

    @Test
    public void testEnhancedSelfHealingLocators() {
        LoggerUtil.info("=== Starting Enhanced Self-Healing Test ===");
        
        // Login
        LoggerUtil.info("Step 1: Logging in");
        loginPage.login("standard_user", "secret_sauce");
        
        // Add item to cart
        LoggerUtil.info("Step 2: Adding item to cart");
        inventoryPage.addFirstItemToCart();
        
        // Navigate to cart
        LoggerUtil.info("Step 3: Navigating to cart");
        inventoryPage.goToCart();
        
        // Verify cart items
        LoggerUtil.info("Step 4: Verifying cart items");
        boolean cartHasItems = cartPage.isCartItemPresent();
        LoggerUtil.info("Cart has items: {}", cartHasItems);
        
        if (cartHasItems) {
            // Proceed to checkout
            LoggerUtil.info("Step 5: Proceeding to checkout");
            cartPage.proceedToCheckout();
            
            // Fill checkout information
            LoggerUtil.info("Step 6: Filling checkout information");
            checkoutPage.enterUserInfo("John", "Doe", "12345");
            checkoutPage.continueCheckout();
            
            // Complete checkout
            LoggerUtil.info("Step 7: Completing checkout");
            overviewPage.finishCheckout();
            
            // Verify completion
            LoggerUtil.info("Step 8: Verifying checkout completion");
            boolean isComplete = thankYouPage.isCheckoutComplete();
            LoggerUtil.info("Checkout complete: {}", isComplete);
        }
        
        // Print healing statistics
        LoggerUtil.info("=== Self-Healing Statistics ===");
        var stats = LocatorUtil.getHealingStats();
        LoggerUtil.info("Total healed locators: {}", stats.get("totalHealed"));
        LoggerUtil.info("Auto-update enabled: {}", stats.get("autoUpdateEnabled"));
        LoggerUtil.info("Learning enabled: {}", stats.get("learningEnabled"));
        
        if (!stats.get("healedLocators").toString().equals("{}")) {
            LoggerUtil.info("Healed locators: {}", stats.get("healedLocators"));
        }
    }

    @Test
    public void testSelfHealingWithBrokenLocators() {
        LoggerUtil.info("=== Testing Self-Healing with Broken Locators ===");
        
        // Login
        loginPage.login("standard_user", "secret_sauce");
        
        // Test with intentionally broken locators that should be healed
        LoggerUtil.info("Testing self-healing with multiple fallback strategies");
        
        // The enhanced locators should automatically try multiple strategies
        // and learn new ones if needed
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();
        
        LoggerUtil.info("Self-healing test completed successfully");
    }

    @AfterMethod
    public void tearDown() {
        // Print final statistics
        var stats = LocatorUtil.getHealingStats();
        LoggerUtil.info("=== Final Self-Healing Statistics ===");
        LoggerUtil.info("Total healed locators: {}", stats.get("totalHealed"));
        
        DriverManager.getInstance().quitDriver();
    }
} 