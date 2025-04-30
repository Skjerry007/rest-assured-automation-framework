package com.seleniumautomation.locators;

/**
 * AmazonLocators - Centralized storage for XPath expressions
 */
public class AmazonLocators {
    // Home Page Locators
    public static final String SEARCH_BOX = "//input[@id='twotabsearchtextbox']";
    public static final String SEARCH_BUTTON = "//input[@id='nav-search-submit-button']";
    public static final String ACCOUNT_LINK = "//a[@id='nav-link-accountList']";
    public static final String CART_LINK = "//a[@id='nav-cart']";
    public static final String HAMBURGER_MENU = "//a[@id='nav-hamburger-menu']";
    
    // Search Results Page Locators
    public static final String SEARCH_RESULTS = "//div[@data-component-type='s-search-result']";
    public static final String SORT_DROPDOWN = "//select[@id='s-result-sort-select']";
    public static final String PAGINATION_BUTTONS = "//a[@class='s-pagination-item']";
    public static final String PRODUCT_TITLE = "//span[@class='a-size-medium']";
    public static final String PRODUCT_PRICE = "//span[@class='a-price']//span[@class='a-offscreen']";
    
    // Product Page Locators
    public static final String ADD_TO_CART_BUTTON = "//input[@id='add-to-cart-button']";
    public static final String BUY_NOW_BUTTON = "//input[@id='buy-now-button']";
    public static final String PRODUCT_TITLE_DETAIL = "//span[@id='productTitle']";
    public static final String PRODUCT_PRICE_DETAIL = "//span[@id='priceblock_ourprice']";
    public static final String QUANTITY_DROPDOWN = "//select[@id='quantity']";
    
    private AmazonLocators() {
        // Private constructor to prevent instantiation
    }
}