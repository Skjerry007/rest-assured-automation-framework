package com.seleniumautomation.base;

import com.seleniumautomation.config.ConfigManager;
import com.seleniumautomation.driver.DriverManager;
import com.restautomation.utils.LoggerUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Base class for all test classes
 */
public class BaseTest {
    
    @BeforeMethod
    public void setUp() {
        try {
            // Initialize WebDriver
            DriverManager.getInstance().initializeDriver();
            
            // Navigate to base URL
            String baseUrl = ConfigManager.getInstance().getProperty("web.baseUrl");
            DriverManager.getInstance().getDriver().get(baseUrl);
            LoggerUtil.info("Navigated to base URL: {}", baseUrl);
            
        } catch (Exception e) {
            LoggerUtil.error("Error in test setup: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterMethod
    public void tearDown() {
        try {
            // Quit WebDriver
            DriverManager.getInstance().quitDriver();
        } catch (Exception e) {
            LoggerUtil.error("Error in test teardown: {}", e.getMessage());
        }
    }
}