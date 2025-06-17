package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.driver.DriverManager;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

/**
 * AmazonHomePage - Page Object for Amazon home page
 */
public class AmazonHomePage {
    private final SeleniumKeywords keywords;
    
    public AmazonHomePage() {
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Search for product
     * @param productName Product to search for
     */
    public void searchProduct(String productName) {
        String searchBoxXpath = LocatorUtil.getLocator("AmazonLocators", "SEARCH_BOX");
        String searchButtonXpath = LocatorUtil.getLocator("AmazonLocators", "SEARCH_BUTTON");
        LoggerUtil.info("Searching for product: {}", productName);
        WebElement searchBox = DriverManager.getInstance().getDriver().findElement(By.xpath(searchBoxXpath));
        WebElement searchButton = DriverManager.getInstance().getDriver().findElement(By.xpath(searchButtonXpath));
        keywords.typeText(searchBox, productName);
        keywords.clickElement(searchButton);
    }
    
    /**
     * Navigate to account
     */
    public void navigateToAccount() {
        String accountLinkXpath = LocatorUtil.getLocator("AmazonLocators", "ACCOUNT_LINK");
        WebElement accountLink = DriverManager.getInstance().getDriver().findElement(By.xpath(accountLinkXpath));
        LoggerUtil.info("Navigating to account");
        keywords.clickElement(accountLink);
    }
    
    /**
     * Navigate to cart
     */
    public void navigateToCart() {
        String cartLinkXpath = LocatorUtil.getLocator("AmazonLocators", "CART_ICON");
        WebElement cartLink = DriverManager.getInstance().getDriver().findElement(By.xpath(cartLinkXpath));
        LoggerUtil.info("Navigating to cart");
        keywords.clickElement(cartLink);
    }
    
    /**
     * Open hamburger menu
     */
    public void openHamburgerMenu() {
        String hamburgerMenuXpath = LocatorUtil.getLocator("AmazonLocators", "HAMBURGER_MENU");
        WebElement hamburgerMenu = DriverManager.getInstance().getDriver().findElement(By.xpath(hamburgerMenuXpath));
        LoggerUtil.info("Opening hamburger menu");
        keywords.clickElement(hamburgerMenu);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        String searchBoxXpath = LocatorUtil.getLocator("AmazonLocators", "SEARCH_BOX");
        String accountLinkXpath = LocatorUtil.getLocator("AmazonLocators", "ACCOUNT_LINK");
        String cartLinkXpath = LocatorUtil.getLocator("AmazonLocators", "CART_ICON");
        WebElement searchBox = DriverManager.getInstance().getDriver().findElement(By.xpath(searchBoxXpath));
        WebElement accountLink = DriverManager.getInstance().getDriver().findElement(By.xpath(accountLinkXpath));
        WebElement cartLink = DriverManager.getInstance().getDriver().findElement(By.xpath(cartLinkXpath));
        return keywords.isElementDisplayed(searchBox) && 
               keywords.isElementDisplayed(accountLink) && 
               keywords.isElementDisplayed(cartLink);
    }
}