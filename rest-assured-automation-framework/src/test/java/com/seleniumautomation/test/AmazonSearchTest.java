package com.seleniumautomation.tests;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.pages.AmazonHomePage;
import com.seleniumautomation.pages.AmazonSearchResultsPage;
import com.seleniumautomation.pages.AmazonProductPage;
import com.restautomation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * AmazonSearchTest - Tests for Amazon search functionality
 */
public class AmazonSearchTest extends BaseTest {
    
    @Test(description = "Test product search and selection")
    public void testProductSearch() {
        AmazonHomePage homePage = new AmazonHomePage();
        Assert.assertTrue(homePage.isPageLoaded(), "Home page not loaded");
        
        // Search for a product
        String searchTerm = "laptop";
        homePage.searchProduct(searchTerm);
        LoggerUtil.info("Searched for product: {}", searchTerm);
        
        // Verify search results
        AmazonSearchResultsPage resultsPage = new AmazonSearchResultsPage();
        Assert.assertTrue(resultsPage.isPageLoaded(), "Search results page not loaded");
        Assert.assertTrue(resultsPage.getNumberOfResults() > 0, "No search results found");
        
        // Sort results by price high to low
        resultsPage.sortResults("Price: High to Low");
        
        // Click first result
        resultsPage.clickResult(0);
        
        // Verify product page
        AmazonProductPage productPage = new AmazonProductPage();
        Assert.assertTrue(productPage.isPageLoaded(), "Product page not loaded");
        
        // Get product details
        String productTitle = productPage.getProductTitle();
        String productPrice = productPage.getProductPrice();
        LoggerUtil.info("Selected product: {} - Price: {}", productTitle, productPrice);
        
        // Add to cart
        productPage.addToCart();
    }
}