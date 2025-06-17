package com.seleniumautomation.pages;

import com.seleniumautomation.keywords.SeleniumKeywords;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.seleniumautomation.driver.DriverManager;
import com.seleniumautomation.utils.LocatorUtil;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.By;

import java.util.List;

/**
 * AmazonSearchResultsPage - Page Object for Amazon search results
 */
public class AmazonSearchResultsPage {
    private final SeleniumKeywords keywords;
    
    public AmazonSearchResultsPage() {
        PageFactory.initElements(DriverManager.getInstance().getDriver(), this);
        this.keywords = new SeleniumKeywords();
    }
    
    /**
     * Get number of search results
     * @return Number of results
     */
    public int getNumberOfResults() {
        String resultItemXpath = LocatorUtil.getLocator("AmazonLocators", "RESULT_ITEM");
        List<WebElement> results = DriverManager.getInstance().getDriver().findElements(By.xpath(resultItemXpath));
        return results.size();
    }
    
    /**
     * Click specific result
     * @param index Result index (0-based)
     */
    public void clickResult(int index) {
        String resultItemXpath = LocatorUtil.getLocator("AmazonLocators", "RESULT_ITEM");
        List<WebElement> results = DriverManager.getInstance().getDriver().findElements(By.xpath(resultItemXpath));
        if (index < results.size()) {
            LoggerUtil.info("Clicking result at index {}", index);
            results.get(index).click();
        } else {
            throw new IllegalArgumentException("Invalid result index: " + index);
        }
    }
    
    /**
     * Sort results
     * @param sortOption Sort option text
     */
    public void sortResults(String sortOption) {
        String sortDropdownXpath = LocatorUtil.getLocator("AmazonLocators", "SORT_DROPDOWN");
        WebElement sortDropdown = DriverManager.getInstance().getDriver().findElement(By.xpath(sortDropdownXpath));
        LoggerUtil.info("Sorting results by option: {}", sortOption);
        sortDropdown.click(); // or use keywords if needed
    }
    
    /**
     * Go to next page
     */
    public void goToNextPage() {
        String paginationButtonXpath = LocatorUtil.getLocator("AmazonLocators", "PAGINATION_BUTTONS");
        List<WebElement> paginationButtons = DriverManager.getInstance().getDriver().findElements(By.xpath(paginationButtonXpath));
        if (!paginationButtons.isEmpty()) {
            LoggerUtil.info("Going to next page");
            paginationButtons.get(paginationButtons.size() - 1).click();
        }
    }
    
    /**
     * Get product title at index
     * @param index Result index
     * @return Product title
     */
    public String getProductTitle(int index) {
        String productTitleXpath = LocatorUtil.getLocator("AmazonLocators", "PRODUCT_TITLE");
        List<WebElement> productTitles = DriverManager.getInstance().getDriver().findElements(By.xpath(productTitleXpath));
        if (index < productTitles.size()) {
            return productTitles.get(index).getText();
        }
        throw new IllegalArgumentException("Invalid product index: " + index);
    }
    
    /**
     * Get product price at index
     * @param index Result index
     * @return Product price
     */
    public String getProductPrice(int index) {
        String productPriceXpath = LocatorUtil.getLocator("AmazonLocators", "PRODUCT_PRICE");
        List<WebElement> productPrices = DriverManager.getInstance().getDriver().findElements(By.xpath(productPriceXpath));
        if (index < productPrices.size()) {
            return productPrices.get(index).getText();
        }
        throw new IllegalArgumentException("Invalid product index: " + index);
    }
    
    /**
     * Check if page is loaded
     * @return true if loaded
     */
    public boolean isPageLoaded() {
        String resultItemXpath = LocatorUtil.getLocator("AmazonLocators", "RESULT_ITEM");
        String sortDropdownXpath = LocatorUtil.getLocator("AmazonLocators", "SORT_DROPDOWN");
        List<WebElement> results = DriverManager.getInstance().getDriver().findElements(By.xpath(resultItemXpath));
        WebElement sortDropdown = DriverManager.getInstance().getDriver().findElement(By.xpath(sortDropdownXpath));
        return !results.isEmpty() && sortDropdown.isDisplayed();
    }
    
    public void selectFirstResult() {
        String resultItem = LocatorUtil.getLocator("AmazonLocators", "RESULT_ITEM");
        LoggerUtil.info("Selecting the first search result");
        DriverManager.getInstance().getDriver().findElements(By.xpath(resultItem)).get(0).click();
    }
}