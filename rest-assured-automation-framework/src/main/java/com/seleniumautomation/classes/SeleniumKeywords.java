package com.seleniumautomation.keywords;

import com.seleniumautomation.config.ConfigManager;
import com.seleniumautomation.driver.DriverManager;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SeleniumKeywords - Common Selenium operations wrapped in reusable methods
 */
public class SeleniumKeywords {
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WaitUtil wait;
    
    public SeleniumKeywords() {
        this.driver = DriverManager.getInstance().getDriver();
        this.js = (JavascriptExecutor) driver;
        this.wait = new WaitUtil();
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public void navigateToUrl(String url) {
        try {
            LoggerUtil.info("Navigating to URL: {}", url);
            driver.get(url);
        } catch (Exception e) {
            LoggerUtil.error("Error navigating to URL: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate to URL: " + url, e);
        }
    }
    
    /**
     * Click element
     * @param element WebElement to click
     */
    public void clickElement(WebElement element) {
        try {
            wait.waitForElementToBeClickable(element);
            scrollIntoView(element);
            element.click();
            LoggerUtil.info("Clicked element: {}", element);
        } catch (ElementClickInterceptedException e) {
            LoggerUtil.info("Element click intercepted, trying JavaScript click");
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LoggerUtil.error("Error clicking element: {}", e.getMessage());
            throw new RuntimeException("Failed to click element", e);
        }
    }
    
    /**
     * Type text into element
     * @param element WebElement to type into
     * @param text Text to type
     */
    public void typeText(WebElement element, String text) {
        try {
            wait.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            LoggerUtil.info("Typed text '{}' into element: {}", text, element);
        } catch (Exception e) {
            LoggerUtil.error("Error typing text: {}", e.getMessage());
            throw new RuntimeException("Failed to type text", e);
        }
    }
    
    /**
     * Upload file
     * @param element File input element
     * @param filePath Path to file
     */
    public void uploadFile(WebElement element, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            element.sendKeys(file.getAbsolutePath());
            LoggerUtil.info("Uploaded file: {}", filePath);
        } catch (Exception e) {
            LoggerUtil.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    /**
     * Select option by visible text
     * @param element Select element
     * @param text Option text
     */
    public void selectByVisibleText(WebElement element, String text) {
        try {
            wait.waitForElementToBeVisible(element);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            LoggerUtil.info("Selected option '{}' from dropdown", text);
        } catch (Exception e) {
            LoggerUtil.error("Error selecting option: {}", e.getMessage());
            throw new RuntimeException("Failed to select option", e);
        }
    }
    
    /**
     * Handle alert
     * @param accept true to accept, false to dismiss
     * @return Alert text
     */
    public String handleAlert(boolean accept) {
        try {
            Alert alert = wait.waitForAlert();
            String alertText = alert.getText();
            if (accept) {
                alert.accept();
                LoggerUtil.info("Accepted alert with text: {}", alertText);
            } else {
                alert.dismiss();
                LoggerUtil.info("Dismissed alert with text: {}", alertText);
            }
            return alertText;
        } catch (Exception e) {
            LoggerUtil.error("Error handling alert: {}", e.getMessage());
            throw new RuntimeException("Failed to handle alert", e);
        }
    }
    
    /**
     * Switch to window by title
     * @param title Window title
     */
    public void switchToWindowByTitle(String title) {
        try {
            String currentWindow = driver.getWindowHandle();
            Set<String> windows = driver.getWindowHandles();
            
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(title)) {
                    LoggerUtil.info("Switched to window with title: {}", title);
                    return;
                }
            }
            
            driver.switchTo().window(currentWindow);
            throw new RuntimeException("Window with title '" + title + "' not found");
        } catch (Exception e) {
            LoggerUtil.error("Error switching window: {}", e.getMessage());
            throw new RuntimeException("Failed to switch window", e);
        }
    }
    
    /**
     * Switch to frame
     * @param element Frame element
     */
    public void switchToFrame(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            driver.switchTo().frame(element);
            LoggerUtil.info("Switched to frame: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Error switching to frame: {}", e.getMessage());
            throw new RuntimeException("Failed to switch to frame", e);
        }
    }
    
    /**
     * Switch to default content
     */
    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            LoggerUtil.info("Switched to default content");
        } catch (Exception e) {
            LoggerUtil.error("Error switching to default content: {}", e.getMessage());
            throw new RuntimeException("Failed to switch to default content", e);
        }
    }
    
    /**
     * Scroll element into view
     * @param element Element to scroll to
     */
    public void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            // Add small delay to allow scroll to complete
            Thread.sleep(500);
            LoggerUtil.info("Scrolled element into view: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Error scrolling element into view: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll element into view", e);
        }
    }
    
    /**
     * Hover over element
     * @param element Element to hover over
     */
    public void hoverOverElement(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            LoggerUtil.info("Hovered over element: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Error hovering over element: {}", e.getMessage());
            throw new RuntimeException("Failed to hover over element", e);
        }
    }
    
    /**
     * Get element text
     * @param element Element to get text from
     * @return Element text
     */
    public String getElementText(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            String text = element.getText();
            LoggerUtil.info("Got text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            LoggerUtil.error("Error getting element text: {}", e.getMessage());
            throw new RuntimeException("Failed to get element text", e);
        }
    }
    
    /**
     * Check if element is displayed
     * @param element Element to check
     * @return true if displayed
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            LoggerUtil.info("Element {} is {}", element, isDisplayed ? "displayed" : "not displayed");
            return isDisplayed;
        } catch (Exception e) {
            LoggerUtil.info("Element is not displayed: {}", element);
            return false;
        }
    }
    
    /**
     * Wait for element to disappear
     * @param element Element to wait for
     */
    public void waitForElementToDisappear(WebElement element) {
        try {
            wait.waitForElementToDisappear(element);
            LoggerUtil.info("Element disappeared: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Error waiting for element to disappear: {}", e.getMessage());
            throw new RuntimeException("Element did not disappear", e);
        }
    }
    
    /**
     * Set browser cookie
     * @param name Cookie name
     * @param value Cookie value
     */
    public void setBrowserCookie(String name, String value) {
        try {
            Cookie cookie = new Cookie(name, value);
            driver.manage().addCookie(cookie);
            LoggerUtil.info("Set browser cookie: {}={}", name, value);
        } catch (Exception e) {
            LoggerUtil.error("Error setting browser cookie: {}", e.getMessage());
            throw new RuntimeException("Failed to set browser cookie", e);
        }
    }
    
    /**
     * Delete all cookies
     */
    public void deleteAllCookies() {
        try {
            driver.manage().deleteAllCookies();
            LoggerUtil.info("Deleted all browser cookies");
        } catch (Exception e) {
            LoggerUtil.error("Error deleting cookies: {}", e.getMessage());
            throw new RuntimeException("Failed to delete cookies", e);
        }
    }
    
    /**
     * Refresh page
     */
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            LoggerUtil.info("Refreshed page");
        } catch (Exception e) {
            LoggerUtil.error("Error refreshing page: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh page", e);
        }
    }
    
    /**
     * Execute JavaScript
     * @param script JavaScript code
     * @param args Script arguments
     * @return Script result
     */
    public Object executeJavaScript(String script, Object... args) {
        try {
            Object result = js.executeScript(script, args);
            LoggerUtil.info("Executed JavaScript: {}", script);
            return result;
        } catch (Exception e) {
            LoggerUtil.error("Error executing JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript", e);
        }
    }
}