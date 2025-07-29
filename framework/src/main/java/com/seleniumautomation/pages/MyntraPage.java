package com.seleniumautomation.pages;

import com.seleniumautomation.utils.ElementFinder;
import com.seleniumautomation.utils.LocatorUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MyntraPage {
    private final WebDriver driver;
    private final ElementFinder elementFinder;
    private final WebDriverWait wait;

    public MyntraPage(WebDriver driver) {
        this.driver = driver;
        this.elementFinder = new ElementFinder(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public void searchForProduct(String query) {
        List<org.openqa.selenium.By> searchBarLocators = LocatorUtil.getLocators("searchBar");
        WebElement searchBar = elementFinder.findElementWithRetries(searchBarLocators);
        wait.until(ExpectedConditions.visibilityOf(searchBar));
        searchBar.click();
        searchBar.clear();
        searchBar.sendKeys(query);
        searchBar.sendKeys(Keys.ENTER);
    }

    public void closeCookiePopupIfPresent() {
        try {
            List<org.openqa.selenium.By> popupLocators = LocatorUtil.getLocators("cookiePopupClose");
            for (By by : popupLocators) {
                List<WebElement> buttons = driver.findElements(by);
                for (WebElement btn : buttons) {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        btn.click();
                        return;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public void switchToNewWindow() {
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                return;
            }
        }
    }

    public void clickFirstProduct() {
        List<org.openqa.selenium.By> firstProductLocators = LocatorUtil.getLocators("firstProduct");
        WebElement firstProduct = elementFinder.findElementWithRetries(firstProductLocators);
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct));
        firstProduct.click();
        // Switch to new window if opened
        switchToNewWindow();
        // Wait for size section or PDP to load
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("p.size-buttons-unified-size")));
    }

    public void selectSize() {
        List<org.openqa.selenium.By> sizeLocators = LocatorUtil.getLocators("selectSize");
        // Find all matching size elements
        for (By by : sizeLocators) {
            List<WebElement> sizes = driver.findElements(by);
            for (WebElement sizeElem : sizes) {
                if (sizeElem.isDisplayed() && sizeElem.isEnabled()) {
                    wait.until(ExpectedConditions.elementToBeClickable(sizeElem));
                    sizeElem.click();
                    return;
                }
            }
        }
        throw new RuntimeException("No available size button found");
    }

    public void addToBag() {
        List<org.openqa.selenium.By> locators = LocatorUtil.getLocators("addToBag");
        WebElement addToBagElem = elementFinder.findElementWithRetries(locators);
        wait.until(ExpectedConditions.elementToBeClickable(addToBagElem));
        addToBagElem.click();
        // Wait for 'GO TO BAG' to appear
        List<org.openqa.selenium.By> goToBagLocators = LocatorUtil.getLocators("goToBag");
        boolean goToBagAppeared = false;
        for (By by : goToBagLocators) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                goToBagAppeared = true;
                break;
            } catch (Exception ignored) {}
        }
        if (!goToBagAppeared) {
            throw new RuntimeException("'GO TO BAG' button did not appear after clicking 'Add to Bag'");
        }
    }

    public void goToBag() {
        List<org.openqa.selenium.By> locators = LocatorUtil.getLocators("goToBag");
        WebElement goToBagElem = elementFinder.findElementWithRetries(locators);
        wait.until(ExpectedConditions.elementToBeClickable(goToBagElem));
        goToBagElem.click();
    }

    public void openBag() {
        List<org.openqa.selenium.By> locators = LocatorUtil.getLocators("openBag");
        WebElement openBagElem = elementFinder.findElementWithRetries(locators);
        wait.until(ExpectedConditions.elementToBeClickable(openBagElem));
        openBagElem.click();
    }

    public String getProductNameOnPDP() {
        try {
            WebElement nameElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pdp-title")));
            return nameElem.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getProductNameInBag() {
        List<org.openqa.selenium.By> locators = LocatorUtil.getLocators("bagProductName");
        WebElement bagProduct = elementFinder.findElementWithRetries(locators);
        wait.until(ExpectedConditions.visibilityOf(bagProduct));
        return bagProduct.getText();
    }
} 