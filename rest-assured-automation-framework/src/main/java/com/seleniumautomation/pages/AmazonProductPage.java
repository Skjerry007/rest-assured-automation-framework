package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.locators.AmazonLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.seleniumautomation.driver.DriverManager;

/**
 * AmazonProductPage - Page Object for Amazon product page
 */
public class AmazonProductPage {
    private final SeleniumKeywords keywords;
    
    @FindBy(xpath = AmazonLocators.ADD_TO_CART_BUTTON)
    private WebElement addToCartButton;
    
    @FindBy(xpath = AmazonLocators.BUY_NOW_BUTTON)
    private WebElement buyNowButton;
    
    @FindBy(xpath = AmazonLocators.PRODUCT_TITLE_DETAIL)
    private WebElement productTitle;
    
    @FindBy(xpath = AmazonLocators.PRODUCT_PRICE_DETAIL)
    private WebElement productPrice;
    
    @FindBy(xpath = AmazonLocators.QUANTITY_DROPDOWN)
    private WebElement quantityDropdown;
    
    public AmazonProductPage() {
        PageFactory.initElements(DriverManager.getInstance().getDriver(), this);
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Add product to cart
     */
    public void addToCart() {
        keywords.clickElement(addToCartButton);
    }
    
    /**
     * Buy product now
     */
    public void buyNow() {
        keywords.clickElement(buyNowButton);
    }
    
    /**
     * Get product title
     * @return Product title
     */
    public String getProductTitle() {
        return keywords.getElementText(productTitle);
    }
    
    /**
     * Get product price
     * @return Product price
     */
    public String getProductPrice() {
        return keywords.getElementText(productPrice);
    }
    
    /**
     * Set quantity
     * @param quantity Quantity to set
     */
    public void setQuantity(String quantity) {
        keywords.selectByVisibleText(quantityDropdown, quantity);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        return keywords.isElementDisplayed(productTitle) && 
               keywords.isElementDisplayed(addToCartButton);
    }
}