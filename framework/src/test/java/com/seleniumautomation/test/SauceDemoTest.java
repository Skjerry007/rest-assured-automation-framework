package com.seleniumautomation.test;

import com.seleniumautomation.pages.SauceDemoLoginPage;
import com.seleniumautomation.pages.SauceDemoInventoryPage;
import com.seleniumautomation.pages.SauceDemoCartPage;
import com.seleniumautomation.pages.SauceDemoCheckoutPage;
import com.seleniumautomation.config.ConfigManager;
import com.seleniumautomation.driver.WebDriverSetup;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.pages.SauceDemoCheckoutOverviewPage;
import com.seleniumautomation.pages.SauceDemoThankYouPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class SauceDemoTest {
    private WebDriver driver;
    private SauceDemoLoginPage loginPage;
    private SauceDemoInventoryPage inventoryPage;
    private SauceDemoCartPage cartPage;
    private SauceDemoCheckoutPage checkoutPage;
    private SauceDemoCheckoutOverviewPage overviewPage;
    private SauceDemoThankYouPage thankYouPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverSetup.setupDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new SauceDemoLoginPage(driver);
        inventoryPage = new SauceDemoInventoryPage(driver);
        cartPage = new SauceDemoCartPage(driver);
        checkoutPage = new SauceDemoCheckoutPage(driver);
        overviewPage = new SauceDemoCheckoutOverviewPage(driver);
        thankYouPage = new SauceDemoThankYouPage(driver);
    }

    @Test(retryAnalyzer = com.restautomation.utils.RetryAnalyzer.class)
    public void testSauceDemoLoginAndCheckout() {
        String username = ConfigManager.getInstance().getProperty("saucedemo.username", "standard_user");
        String password = ConfigManager.getInstance().getProperty("saucedemo.password", "secret_sauce");

        LoggerUtil.info("Logging in to SauceDemo");
        loginPage.login(username, password);
        Assert.assertFalse(loginPage.isErrorDisplayed(), "Login error displayed!");

        LoggerUtil.info("Adding item to cart");
        String expectedItemName = inventoryPage.getFirstItemName();
        String expectedItemDesc = inventoryPage.getFirstItemDescription();
        String expectedItemPrice = inventoryPage.getFirstItemPrice();
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isCartItemPresent(), "Cart item not present!");

        LoggerUtil.info("Proceeding to checkout");
        cartPage.proceedToCheckout();
        checkoutPage.enterUserInfo("Test", "User", "12345");
        checkoutPage.continueCheckout();

        // Extract and assert all order data
        Assert.assertEquals(overviewPage.getItemName(), expectedItemName, "Item name mismatch");
        Assert.assertEquals(overviewPage.getItemDescription(), expectedItemDesc, "Item description mismatch");
        Assert.assertEquals(overviewPage.getItemPrice(), expectedItemPrice, "Item price mismatch");
        LoggerUtil.info("Order summary: Item: {} | Desc: {} | Price: {} | Payment: {} | Shipping: {} | Total: {} | Tax: {} | Grand Total: {}",
            overviewPage.getItemName(), overviewPage.getItemDescription(), overviewPage.getItemPrice(),
            overviewPage.getPaymentInfo(), overviewPage.getShippingInfo(), overviewPage.getItemTotal(),
            overviewPage.getTax(), overviewPage.getTotal());
        overviewPage.clickFinish();

        // Thank you page assertions
        Assert.assertTrue(thankYouPage.getThankYouHeader().contains("Thank you for your order!"), "Thank you header missing");
        LoggerUtil.info("Thank you message: {}", thankYouPage.getThankYouMessage());
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            {"", "", true},
            {"invalid_user", "secret_sauce", true},
            {"standard_user", "wrong_pass", true}
        };
    }

    @Test(dataProvider = "invalidLoginData")
    public void testInvalidLogin(String username, String password, boolean expectError) {
        LoggerUtil.info("Testing invalid login: {} / {}", username, password);
        loginPage.login(username, password);
        Assert.assertEquals(loginPage.isErrorDisplayed(), expectError, "Login error state mismatch");
    }

    @DataProvider(name = "invalidCheckoutData")
    public Object[][] invalidCheckoutData() {
        return new Object[][] {
            {"", "User", "12345", true},
            {"Test", "", "12345", true},
            {"Test", "User", "", true}
        };
    }

    @Test(dataProvider = "invalidCheckoutData")
    public void testInvalidCheckoutInfo(String firstName, String lastName, String postalCode, boolean expectError) {
        String username = ConfigManager.getInstance().getProperty("saucedemo.username", "standard_user");
        String password = ConfigManager.getInstance().getProperty("saucedemo.password", "secret_sauce");
        loginPage.login(username, password);
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();
        cartPage.proceedToCheckout();
        checkoutPage.enterUserInfo(firstName, lastName, postalCode);
        checkoutPage.continueCheckout();
        Assert.assertEquals(checkoutPage.isErrorDisplayed(), expectError, "Checkout error state mismatch");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}