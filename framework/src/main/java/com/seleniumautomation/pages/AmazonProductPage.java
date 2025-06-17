package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.seleniumautomation.driver.DriverManager;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.By;

/**
 * AmazonProductPage - Page Object for Amazon product page
 */
public class AmazonProductPage {
    private final SeleniumKeywords keywords;
    
    public AmazonProductPage() {
        PageFactory.initElements(DriverManager.getInstance().getDriver(), this);
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Add product to cart
     */
    public void addToCart() {
        String addToCartButtonXpath = LocatorUtil.getLocator("AmazonLocators", "ADD_TO_CART_BUTTON");
        LoggerUtil.info("Clicking Add to Cart button");
        WebElement addToCartButton = DriverManager.getInstance().getDriver().findElement(By.xpath(addToCartButtonXpath));
        keywords.clickElement(addToCartButton);
    }
    
    /**
     * Buy product now
     */
    public void buyNow() {
        String buyNowButtonXpath = LocatorUtil.getLocator("AmazonLocators", "BUY_NOW_BUTTON");
        LoggerUtil.info("Clicking Buy Now button");
        WebElement buyNowButton = DriverManager.getInstance().getDriver().findElement(By.xpath(buyNowButtonXpath));
        keywords.clickElement(buyNowButton);
    }
    
    /**
     * Get product title
     * @return Product title
     */
    public String getProductTitle() {
        String productTitleXpath = LocatorUtil.getLocator("AmazonLocators", "PRODUCT_TITLE");
        WebElement productTitle = DriverManager.getInstance().getDriver().findElement(By.xpath(productTitleXpath));
        return keywords.getElementText(productTitle);
    }
    
    /**
     * Get product price
     * @return Product price
     */
    public String getProductPrice() {
        String productPriceXpath = LocatorUtil.getLocator("AmazonLocators", "PRODUCT_PRICE");
        WebElement productPrice = DriverManager.getInstance().getDriver().findElement(By.xpath(productPriceXpath));
        return keywords.getElementText(productPrice);
    }
    
    /**
     * Set quantity
     * @param quantity Quantity to set
     */
    public void setQuantity(String quantity) {
        String quantityDropdownXpath = LocatorUtil.getLocator("AmazonLocators", "QUANTITY_DROPDOWN");
        WebElement quantityDropdown = DriverManager.getInstance().getDriver().findElement(By.xpath(quantityDropdownXpath));
        LoggerUtil.info("Setting quantity to {}", quantity);
        keywords.selectByVisibleText(quantityDropdown, quantity);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        String productTitleXpath = LocatorUtil.getLocator("AmazonLocators", "PRODUCT_TITLE");
        String addToCartButtonXpath = LocatorUtil.getLocator("AmazonLocators", "ADD_TO_CART_BUTTON");
        WebElement productTitle = DriverManager.getInstance().getDriver().findElement(By.xpath(productTitleXpath));
        WebElement addToCartButton = DriverManager.getInstance().getDriver().findElement(By.xpath(addToCartButtonXpath));
        return keywords.isElementDisplayed(productTitle) && 
               keywords.isElementDisplayed(addToCartButton);
    }
}