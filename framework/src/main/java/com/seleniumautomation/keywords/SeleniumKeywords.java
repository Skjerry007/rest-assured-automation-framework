package com.seleniumautomation.keywords;

import com.seleniumautomation.driver.DriverManager;
import com.restautomation.utils.LoggerUtil;
import com.seleniumautomation.utils.WaitUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
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
        this.js = (JavascriptExecutor) this.driver;
        this.wait = new WaitUtil();
    }

    // Overload for clickElement using By
public void clickElement(By locator) {
    WebElement element = driver.findElement(locator);
    clickElement(element);
}

// Overload for typeText using By
public void typeText(By locator, String text) {
    WebElement element = driver.findElement(locator);
    typeText(element, text);
}

// Overload for isElementDisplayed using By
public boolean isElementDisplayed(By locator) {
    WebElement element = driver.findElement(locator);
    return isElementDisplayed(element);
}

    /**
     * Navigate to a given URL
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
     * Click the given element
     */
    public void clickElement(WebElement element) {
        try {
            wait.waitForElementToBeClickable(element);
            scrollIntoView(element);
            element.click();
            LoggerUtil.info("Clicked element: {}", element);
        } catch (ElementClickInterceptedException e) {
            LoggerUtil.warn("Click intercepted. Trying JavaScript click.");
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LoggerUtil.error("Click failed: {}", e.getMessage());
            throw new RuntimeException("Failed to click element", e);
        }
    }

    /**
     * Type text into the given element
     */
    public void typeText(WebElement element, String text) {
        try {
            wait.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            LoggerUtil.info("Typed text '{}' into element: {}", text, element);
        } catch (Exception e) {
            LoggerUtil.error("Typing text failed: {}", e.getMessage());
            throw new RuntimeException("Failed to type text", e);
        }
    }

    /**
     * Upload a file using WebElement
     */
    public void uploadFile(WebElement element, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) throw new RuntimeException("File not found: " + filePath);
            element.sendKeys(file.getAbsolutePath());
            LoggerUtil.info("Uploaded file: {}", filePath);
        } catch (Exception e) {
            LoggerUtil.error("File upload failed: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    /**
     * Overloaded method - Upload a file using a By locator
     */
    public void uploadFile(By locator, String filePath) {
        WebElement element = driver.findElement(locator);
        uploadFile(element, filePath);
    }

    /**
     * Select dropdown by visible text
     */
    public void selectByVisibleText(WebElement element, String text) {
        try {
            wait.waitForElementToBeVisible(element);
            new Select(element).selectByVisibleText(text);
            LoggerUtil.info("Selected '{}' from dropdown", text);
        } catch (Exception e) {
            LoggerUtil.error("Dropdown selection failed: {}", e.getMessage());
            throw new RuntimeException("Failed to select option", e);
        }
    }

    /**
     * Handle alert - accept or dismiss
     */
    public String handleAlert(boolean accept) {
        try {
            Alert alert = wait.waitForAlert();
            String alertText = alert.getText();
            if (accept) {
                alert.accept();
                LoggerUtil.info("Accepted alert: {}", alertText);
            } else {
                alert.dismiss();
                LoggerUtil.info("Dismissed alert: {}", alertText);
            }
            return alertText;
        } catch (Exception e) {
            LoggerUtil.error("Alert handling failed: {}", e.getMessage());
            throw new RuntimeException("Failed to handle alert", e);
        }
    }

    /**
     * Switch to a window by title
     */
    public void switchToWindowByTitle(String title) {
        try {
            String current = driver.getWindowHandle();
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(title)) {
                    LoggerUtil.info("Switched to window with title: {}", title);
                    return;
                }
            }
            driver.switchTo().window(current);
            throw new RuntimeException("Window with title '" + title + "' not found");
        } catch (Exception e) {
            LoggerUtil.error("Switching window failed: {}", e.getMessage());
            throw new RuntimeException("Failed to switch window", e);
        }
    }

    /**
     * Switch to a frame
     */
    public void switchToFrame(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            driver.switchTo().frame(element);
            LoggerUtil.info("Switched to frame: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Switching to frame failed: {}", e.getMessage());
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
            LoggerUtil.error("Switching to default content failed: {}", e.getMessage());
            throw new RuntimeException("Failed to switch to default content", e);
        }
    }

    /**
     * Scroll an element into view
     */
    public void scrollIntoView(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(300); // Allow smooth scroll
            LoggerUtil.info("Scrolled to element: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Scroll into view failed: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll element into view", e);
        }
    }

    /**
     * Hover over an element
     */
    public void hoverOverElement(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            new Actions(driver).moveToElement(element).perform();
            LoggerUtil.info("Hovered on element: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Hover failed: {}", e.getMessage());
            throw new RuntimeException("Failed to hover over element", e);
        }
    }

    /**
     * Get text from element
     */
    public String getElementText(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            String text = element.getText();
            LoggerUtil.info("Element text: '{}'", text);
            return text;
        } catch (Exception e) {
            LoggerUtil.error("Getting element text failed: {}", e.getMessage());
            throw new RuntimeException("Failed to get element text", e);
        }
    }

    /**
     * Check if element is displayed
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            boolean displayed = element.isDisplayed();
            LoggerUtil.info("Element is displayed: {}", displayed);
            return displayed;
        } catch (Exception e) {
            LoggerUtil.warn("Element not displayed or not found: {}", element);
            return false;
        }
    }

    /**
     * Wait until an element disappears
     */
    public void waitForElementToDisappear(WebElement element) {
        try {
            wait.waitForElementToDisappear(element);
            LoggerUtil.info("Element disappeared: {}", element);
        } catch (Exception e) {
            LoggerUtil.error("Element disappearance failed: {}", e.getMessage());
            throw new RuntimeException("Element did not disappear", e);
        }
    }

    /**
     * Set a browser cookie
     */
    public void setBrowserCookie(String name, String value) {
        try {
            driver.manage().addCookie(new Cookie(name, value));
            LoggerUtil.info("Set cookie: {}={}", name, value);
        } catch (Exception e) {
            LoggerUtil.error("Setting cookie failed: {}", e.getMessage());
            throw new RuntimeException("Failed to set cookie", e);
        }
    }

    /**
     * Delete all browser cookies
     */
    public void deleteAllCookies() {
        try {
            driver.manage().deleteAllCookies();
            LoggerUtil.info("Deleted all cookies");
        } catch (Exception e) {
            LoggerUtil.error("Deleting cookies failed: {}", e.getMessage());
            throw new RuntimeException("Failed to delete cookies", e);
        }
    }

    /**
     * Refresh the browser
     */
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            LoggerUtil.info("Page refreshed");
        } catch (Exception e) {
            LoggerUtil.error("Page refresh failed: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh page", e);
        }
    }

    /**
     * Execute custom JavaScript
     */
    public Object executeJavaScript(String script, Object... args) {
        try {
            Object result = js.executeScript(script, args);
            LoggerUtil.info("Executed JS: {}", script);
            return result;
        } catch (Exception e) {
            LoggerUtil.error("JS execution failed: {}", e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript", e);
        }
    }
}
