package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import com.seleniumautomation.locators.AmazonLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.seleniumautomation.driver.DriverManager;

import java.util.List;

/**
 * AmazonSearchResultsPage - Page Object for Amazon search results
 */
public class AmazonSearchResultsPage {
    private final SeleniumKeywords keywords;
    
    @FindBy(xpath = AmazonLocators.SEARCH_RESULTS)
    private List<WebElement> searchResults;
    
    @FindBy(xpath = AmazonLocators.SORT_DROPDOWN)
    private WebElement sortDropdown;
    
    @FindBy(xpath = AmazonLocators.PAGINATION_BUTTONS)
    private List<WebElement> paginationButtons;
    
    @FindBy(xpath = AmazonLocators.PRODUCT_TITLE)
    private List<WebElement> productTitles;
    
    @FindBy(xpath = AmazonLocators.PRODUCT_PRICE)
    private List<WebElement> productPrices;
    
    public AmazonSearchResultsPage() {
        PageFactory.initElements(DriverManager.getInstance().getDriver(), this);
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Get number of search results
     * @return Number of results
     */
    public int getNumberOfResults() {
        return searchResults.size();
    }
    
    /**
     * Click specific result
     * @param index Result index (0-based)
     */
    public void clickResult(int index) {
        if (index < searchResults.size()) {
            keywords.clickElement(searchResults.get(index));
        } else {
            throw new IllegalArgumentException("Invalid result index: " + index);
        }
    }
    
    /**
     * Sort results
     * @param sortOption Sort option text
     */
    public void sortResults(String sortOption) {
        keywords.selectByVisibleText(sortDropdown, sortOption);
    }
    
    /**
     * Go to next page
     */
    public void goToNextPage() {
        if (!paginationButtons.isEmpty()) {
            keywords.clickElement(paginationButtons.get(paginationButtons.size() - 1));
        }
    }
    
    /**
     * Get product title at index
     * @param index Result index
     * @return Product title
     */
    public String getProductTitle(int index) {
        if (index < productTitles.size()) {
            return keywords.getElementText(productTitles.get(index));
        }
        throw new IllegalArgumentException("Invalid product index: " + index);
    }
    
    /**
     * Get product price at index
     * @param index Result index
     * @return Product price
     */
    public String getProductPrice(int index) {
        if (index < productPrices.size()) {
            return keywords.getElementText(productPrices.get(index));
        }
        throw new IllegalArgumentException("Invalid product index: " + index);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        return !searchResults.isEmpty() && keywords.isElementDisplayed(sortDropdown);
    }
}