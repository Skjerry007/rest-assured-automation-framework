package com.seleniumautomation.test;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.pages.AmazonHomePage;
import com.seleniumautomation.pages.AmazonSearchResultsPage;
import com.seleniumautomation.pages.AmazonProductPage;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.LocatorUtil;
import com.seleniumautomation.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.restautomation.utils.CaptchaReader;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

/**
 * AmazonSearchTest - Tests for Amazon search functionality
 */
public class AmazonSearchTest extends BaseTest {
    
    @Test(description = "Test product search, add to cart, and verify in cart with self-healing locators")
    public void testProductSearch() {
        LoggerUtil.info("Starting Amazon search test");
        WebDriver driver = DriverManager.getInstance().getDriver();
        String searchTerm = "laptop";

        // Wait for Amazon home page or handle CAPTCHA
        boolean captchaCleared = false;
        int maxWaitSeconds = 120;
        int waited = 0;
        while (waited < maxWaitSeconds) {
            try {
                Thread.sleep(5000);
                waited += 5;
                String pageTitle = driver.getTitle();
                String pageUrl = driver.getCurrentUrl();
                LoggerUtil.info("Current page title: {}", pageTitle);
                LoggerUtil.info("Current page URL: {}", pageUrl);
                // Take screenshot at each check
                try {
                    org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                    java.nio.file.Path screenshotPath = java.nio.file.Paths.get("target", "amazon_page_check_" + waited + ".png");
                    java.nio.file.Files.write(screenshotPath, ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
                    LoggerUtil.info("Screenshot saved to: {}", screenshotPath.toString());
                } catch (Exception ex) {
                    LoggerUtil.error("Failed to take screenshot: {}", ex.getMessage());
                }
                // Check for CAPTCHA by title or known element
                if (pageTitle.toLowerCase().contains("robot") || pageTitle.toLowerCase().contains("captcha") || pageUrl.contains("/errors/validateCaptcha")) {
                    LoggerUtil.info("CAPTCHA detected, attempting to solve...");
                    try {
                        CaptchaReader.solveCaptcha(driver);
                        LoggerUtil.info("CAPTCHA solution attempted.");
                        // Wait for page to load after CAPTCHA submission
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        LoggerUtil.error("Failed to solve CAPTCHA: {}", e.getMessage());
                    }
                    continue;
                }
                // Check for home page by presence of search box
                if (driver.findElements(By.xpath("//input[@id='twotabsearchtextbox']")).size() > 0) {
                    LoggerUtil.info("Amazon home page loaded, proceeding with test.");
                    captchaCleared = true;
                    break;
                }
            } catch (Exception e) {
                LoggerUtil.error("Error while waiting for home page or CAPTCHA: {}", e.getMessage());
            }
        }
        if (!captchaCleared) {
            LoggerUtil.error("CAPTCHA not cleared or home page not loaded after waiting. Failing test.");
            throw new RuntimeException("CAPTCHA not cleared or home page not loaded after waiting");
        }

        // Self-healing: Search for a product
        By searchBox = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "SEARCH_BOX");
        String searchBoxXpath = LocatorUtil.getLocator("AmazonLocators", "SEARCH_BOX");
        LoggerUtil.info("SEARCH_BOX locator xpath: {}", searchBoxXpath);
        By searchButton = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "SEARCH_BUTTON");

        // Print page title and URL for debugging
        LoggerUtil.info("Current page title: {}", driver.getTitle());
        LoggerUtil.info("Current page URL: {}", driver.getCurrentUrl());

        try {
            // Wait for search box to be visible and enabled (timeout 30s)
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.and(
                    org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(searchBox),
                    org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(searchBox)
                ));
            driver.findElement(searchBox).sendKeys(searchTerm);
            driver.findElement(searchButton).click();
            LoggerUtil.info("Searched for product: {}", searchTerm);
        } catch (Exception e) {
            LoggerUtil.error("Failed to interact with search box: {}", e.getMessage());
            // Take screenshot
            try {
                org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                java.nio.file.Path screenshotPath = java.nio.file.Paths.get("target", "amazon_searchbox_failure.png");
                java.nio.file.Files.write(screenshotPath, ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
                LoggerUtil.error("Screenshot saved to: {}", screenshotPath.toString());
            } catch (Exception ex) {
                LoggerUtil.error("Failed to take screenshot: {}", ex.getMessage());
            }
            // Dump page source
            try {
                String pageSource = driver.getPageSource();
                java.nio.file.Path pageSourcePath = java.nio.file.Paths.get("target", "amazon_searchbox_failure.html");
                java.nio.file.Files.write(pageSourcePath, pageSource.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                LoggerUtil.error("Page source saved to: {}", pageSourcePath.toString());
            } catch (Exception ex) {
                LoggerUtil.error("Failed to save page source: {}", ex.getMessage());
            }
            throw e;
        }

        // Self-healing: Wait for and click first valid product result
        By resultItem = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "RESULT_ITEM");
        java.util.List<WebElement> results = driver.findElements(resultItem);
        WebElement firstValidResult = null;
        String expectedTitle = null;
        for (WebElement result : results) {
            try {
                WebElement link = result.findElement(By.xpath(".//a[@href and (contains(@class,'a-link-normal') or contains(@class,'s-no-outline'))]"));
                WebElement title = result.findElement(By.xpath(".//span[contains(@class,'a-size-medium') or @id='productTitle']"));
                if (link.isDisplayed() && title.isDisplayed()) {
                    firstValidResult = link;
                    expectedTitle = title.getText();
                    LoggerUtil.info("Found valid product result: {}", expectedTitle);
                    break;
                }
            } catch (Exception e) {
                // Not a valid product result, skip
            }
        }
        if (firstValidResult == null) {
            LoggerUtil.error("No valid product result found in search results.");
            throw new RuntimeException("No valid product result found");
        }
        firstValidResult.click();

        // Wait for product detail page to load
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@id='centerCol'] | //div[@id='rightCol']")
                ));
            LoggerUtil.info("Product detail page loaded");
        } catch (Exception e) {
            LoggerUtil.error("Failed to load product detail page: {}", e.getMessage());
            throw e;
        }

        // Self-healing: Add to cart
        By addToCartButton = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "ADD_TO_CART_BUTTON");
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addToCartButton));
            driver.findElement(addToCartButton).click();
            LoggerUtil.info("Added product to cart: {}", expectedTitle);
        } catch (Exception e) {
            LoggerUtil.error("Failed to add product to cart: {}", e.getMessage());
            // Take screenshot
            try {
                org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                java.nio.file.Path screenshotPath = java.nio.file.Paths.get("target", "amazon_addtocart_failure.png");
                java.nio.file.Files.write(screenshotPath, ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
                LoggerUtil.error("Screenshot saved to: {}", screenshotPath.toString());
            } catch (Exception ex) {
                LoggerUtil.error("Failed to take screenshot: {}", ex.getMessage());
            }
            throw e;
        }

        // Self-healing: Go to cart
        By cartIcon = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "CART_ICON");
        driver.findElement(cartIcon).click();

        // Self-healing: Verify product in cart
        By productTitle = LocatorUtil.getOrHealLocator(driver, "AmazonLocators", "PRODUCT_TITLE");
        String cartProductTitle = driver.findElement(productTitle).getText();
        Assert.assertTrue(cartProductTitle.contains(expectedTitle) || expectedTitle.contains(cartProductTitle), "Product in cart does not match selected product");
        LoggerUtil.info("Verified product in cart: {}", cartProductTitle);
        LoggerUtil.info("Amazon search test completed");
    }
}