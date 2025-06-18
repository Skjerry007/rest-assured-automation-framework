package com.seleniumautomation.test;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.pages.AmazonHomePage;
import com.seleniumautomation.pages.AmazonSearchResultsPage;
import com.seleniumautomation.pages.AmazonProductPage;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.LocatorUtil;
import com.seleniumautomation.utils.CaptchaReader;
import com.seleniumautomation.driver.DriverManager;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

/**
 * AmazonSearchTest - Tests for Amazon search functionality
 */
public class AmazonSearchTest extends BaseTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);
    private static final String AMAZON_URL = "https://www.amazon.in/";
    
    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][] {
            {"books", "Books"},
            {"electronics", "Electronics"},
            {"shoes", "Shoes"}
        };
    }
    
    @Test(dataProvider = "searchData")
    public void testProductSearch(String searchTerm, String category) {
        try {
            WebDriver driver = DriverManager.getInstance().getDriver();
            driver.get(AMAZON_URL);
            LoggerUtil.info("Navigated to Amazon.com");
            
            // Handle CAPTCHA if present
            handleCaptchaIfPresent(driver);
            
            // Perform search
            selectCategoryAndSearch(driver, category, searchTerm);
            
            // Verify search results
            verifySearchResults(driver, searchTerm);
            
            // Add first item to cart
            addFirstItemToCart();
            
        } catch (Exception e) {
            LoggerUtil.error("Test failed: " + e.getMessage());
            CaptchaReader.takeScreenshot(DriverManager.getInstance().getDriver(), "test_failure");
            throw e;
        }
    }
    
    private void handleCaptchaIfPresent(WebDriver driver) {
        try {
            if (CaptchaReader.isCaptchaPresent(driver)) {
                LoggerUtil.info("CAPTCHA detected, attempting to solve...");
                String captchaText = CaptchaReader.solveCaptcha(driver);

                if (captchaText != null && !captchaText.isEmpty()) {
                    LoggerUtil.info("OCR SUCCESS: CAPTCHA text read as: {}", captchaText);
                    WebElement captchaInput = driver.findElement(By.id("captchacharacters"));
                    captchaInput.sendKeys(captchaText);

                    WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
                    submitButton.click();

                    LoggerUtil.info("CAPTCHA submitted");
                    Thread.sleep(2000);
                } else {
                    LoggerUtil.error("OCR FAILURE: Unable to read CAPTCHA text. Skipping test.");
                    Assert.fail("OCR utility could not read CAPTCHA text. Test skipped.");
                }
            } else {
                LoggerUtil.info("No CAPTCHA detected");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error handling CAPTCHA: " + e.getMessage());
        }
    }
    
    private void selectCategoryAndSearch(WebDriver driver, String category, String searchTerm) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        
        try {
            // Wait for search box to be present and clickable
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("twotabsearchtextbox")));
            searchBox.clear();
            searchBox.sendKeys(searchTerm);

            // Try to click the dropdown using different methods
            try {
                // Method 1: Direct click
                WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchDropdownBox")));
                dropdown.click();
            } catch (Exception e) {
                try {
                    // Method 2: JavaScript click
                    WebElement dropdown = driver.findElement(By.id("searchDropdownBox"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
                } catch (Exception e2) {
                    // Method 3: Actions click
                    WebElement dropdown = driver.findElement(By.id("searchDropdownBox"));
                    new org.openqa.selenium.interactions.Actions(driver)
                        .moveToElement(dropdown)
                        .click()
                        .perform();
                }
            }

            // Select category
            WebElement categoryOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[contains(text(), '" + category + "')]")));
            categoryOption.click();

            // Submit search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("nav-search-submit-button")));
            searchButton.click();

            LoggerUtil.info("Search performed with category: " + category + " and term: " + searchTerm);
            
        } catch (Exception e) {
            LoggerUtil.error("Error during search: " + e.getMessage());
            throw e;
        }
    }
    
    private void verifySearchResults(WebDriver driver, String searchTerm) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        
        try {
            // Wait for search results
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".s-result-item")));

            // Verify search results are displayed
            Assert.assertTrue(driver.findElements(By.cssSelector(".s-result-item")).size() > 0,
                "No search results found");

            LoggerUtil.info("Search results verified successfully");
            
        } catch (Exception e) {
            LoggerUtil.error("Error verifying search results: " + e.getMessage());
            throw e;
        }
    }
    
    private void addFirstItemToCart() {
        WebDriver driver = DriverManager.getInstance().getDriver();
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        
        // Click first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("[data-component-type='s-search-result'] h2 a")));
        firstProduct.click();
        
        // Add to cart
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
        addToCartButton.click();
        
        // Verify item added to cart
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-cart-count")));
        String cartCount = driver.findElement(By.id("nav-cart-count")).getText();
        Assert.assertNotEquals(cartCount, "0", "Cart is empty");
        
        LoggerUtil.info("Added item to cart successfully");
    }
}