package com.seleniumautomation.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ElementFinder {
    private final WebDriver driver;
    private final int timeoutSeconds = 15;

    public ElementFinder(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findElementWithRetries(List<By> locators) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(d -> {
            for (By by : locators) {
                try {
                    WebElement element = d.findElement(by);
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {}
            }
            return null;
        });
    }

    public void clickElement(List<By> locators) {
        findElementWithRetries(locators).click();
    }
} 