package ui.base;

import common.config.ConfigManager;
import ui.driver.DriverManager;
import common.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest - Base class managing the thread-safe test execution lifecycle
 */
public class BaseTest {
    
    @BeforeSuite
    public void beforeSuite() {
        LoggerUtil.info("========================================");
        LoggerUtil.info("Starting Selenium Automation Test Suite");
        LoggerUtil.info("========================================");
    }
    
    @AfterSuite
    public void afterSuite() {
        LoggerUtil.info("========================================");
        LoggerUtil.info("Finished Selenium Automation Test Suite");
        LoggerUtil.info("========================================");
    }
    
    @BeforeClass
    public void beforeClass() {
        LoggerUtil.info(">>> Starting Test Class: {}", this.getClass().getSimpleName());
    }
    
    @AfterClass
    public void afterClass() {
        LoggerUtil.info("<<< Finished Test Class: {}", this.getClass().getSimpleName());
    }
    
    @BeforeMethod
    public void setUp() {
        try {
            LoggerUtil.info("Initializing WebDriver for thread: {}", Thread.currentThread().getName());
            
            // Initialize WebDriver (Uses ThreadLocal internally for multithreading safety)
            DriverManager.getInstance().initializeDriver();
            
            // Navigate to base URL
            String baseUrl = ConfigManager.getInstance().getWebUrl();
            getDriver().get(baseUrl);
            LoggerUtil.info("Navigated to base URL: {}", baseUrl);
            
        } catch (Exception e) {
            LoggerUtil.error("Error in test setup: {}", e.getMessage(), e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @AfterMethod
    public void tearDown() {
        try {
            LoggerUtil.info("Quitting WebDriver and cleaning ThreadLocal context for thread: {}", Thread.currentThread().getName());
            // Quit WebDriver and clean ThreadLocal references
            DriverManager.getInstance().quitDriver();
        } catch (Exception e) {
            LoggerUtil.error("Error in test teardown: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Get thread-safe active WebDriver instance
     * @return WebDriver
     */
    protected WebDriver getDriver() {
        return DriverManager.getInstance().getDriver();
    }
}