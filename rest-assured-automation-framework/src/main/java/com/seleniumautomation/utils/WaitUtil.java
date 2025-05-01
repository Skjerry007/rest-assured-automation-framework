package com.seleniumautomation.utils;

import com.seleniumautomation.config.ConfigManager;
import com.seleniumautomation.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.restautomation.utils.LoggerUtil;

import java.time.Duration;

/**
 * WaitUtil - Utility class for handling waits
 */
public class WaitUtil {
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    public WaitUtil() {
        this.driver = DriverManager.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, 
                Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()));
    }
    
    /**
     * Wait for element to be visible
     * @param element Element to wait for
     * @return WebElement
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element not visible after {} seconds: {}", 
                    ConfigManager.getInstance().getExplicitWait(), element);
            throw new RuntimeException("Element not visible", e);
        }
    }
    
    /**
     * Wait for element to be clickable
     * @param element Element to wait for
     * @return WebElement
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element not clickable after {} seconds: {}", 
                    ConfigManager.getInstance().getExplicitWait(), element);
            throw new RuntimeException("Element not clickable", e);
        }
    }
    
    /**
     * Wait for element to disappear
     * @param element Element to wait for
     */
    public void waitForElementToDisappear(WebElement element) {
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element still visible after {} seconds: {}", 
                    ConfigManager.getInstance().getExplicitWait(), element);
            throw new RuntimeException("Element still visible", e);
        }
    }
    
    /**
     * Wait for alert
     * @return Alert
     */
    public Alert waitForAlert() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            LoggerUtil.error("Alert not present after {} seconds", 
                    ConfigManager.getInstance().getExplicitWait());
            throw new RuntimeException("Alert not present", e);
        }
    }
    
    /**
     * Wait for page title
     * @param title Expected title
     */
    public void waitForPageTitle(String title) {
        try {
            wait.until(ExpectedConditions.titleIs(title));
        } catch (TimeoutException e) {
            LoggerUtil.error("Page title not '{}' after {} seconds", title, 
                    ConfigManager.getInstance().getExplicitWait());
            throw new RuntimeException("Page title not as expected", e);
        }
    }
    
    /**
     * Wait for URL to contain
     * @param urlFragment URL fragment to wait for
     */
    public void waitForUrlToContain(String urlFragment) {
        try {
            wait.until(ExpectedConditions.urlContains(urlFragment));
        } catch (TimeoutException e) {
            LoggerUtil.error("URL does not contain '{}' after {} seconds", urlFragment, 
                    ConfigManager.getInstance().getExplicitWait());
            throw new RuntimeException("URL does not contain expected fragment", e);
        }
    }
}