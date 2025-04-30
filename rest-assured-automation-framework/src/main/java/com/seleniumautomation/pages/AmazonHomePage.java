package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.locators.AmazonLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.seleniumautomation.driver.DriverManager;

/**
 * AmazonHomePage - Page Object for Amazon home page
 */
public class AmazonHomePage {
    private final SeleniumKeywords keywords;
    
    @FindBy(xpath = AmazonLocators.SEARCH_BOX)
    private WebElement searchBox;
    
    @FindBy(xpath = AmazonLocators.SEARCH_BUTTON)
    private WebElement searchButton;
    
    @FindBy(xpath = AmazonLocators.ACCOUNT_LINK)
    private WebElement accountLink;
    
    @FindBy(xpath = AmazonLocators.CART_LINK)
    private WebElement cartLink;
    
    @FindBy(xpath = AmazonLocators.HAMBURGER_MENU)
    private WebElement hamburgerMenu;
    
    public AmazonHomePage() {
        PageFactory.initElements(DriverManager.getInstance().getDriver(), this);
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Search for product
     * @param searchTerm Product to search for
     */
    public void searchProduct(String searchTerm) {
        keywords.typeText(searchBox, searchTerm);
        keywords.clickElement(searchButton);
    }
    
    /**
     * Navigate to account
     */
    public void navigateToAccount() {
        keywords.clickElement(accountLink);
    }
    
    /**
     * Navigate to cart
     */
    public void navigateToCart() {
        keywords.clickElement(cartLink);
    }
    
    /**
     * Open hamburger menu
     */
    public void openHamburgerMenu() {
        keywords.clickElement(hamburgerMenu);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        return keywords.isElementDisplayed(searchBox) && 
               keywords.isElementDisplayed(accountLink) && 
               keywords.isElementDisplayed(cartLink);
    }
}