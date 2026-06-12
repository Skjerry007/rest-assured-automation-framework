package com.restautomation.listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.seleniumautomation.driver.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.ByteArrayInputStream;

/**
 * TestListener - TestNG listener for execution events integrated with Allure Reporting
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: " + context.getName());
        System.out.println("\n=======================================================");
        System.out.println(" Allure Results Generated! ");
        System.out.println(" To open and view the report, run the following command:");
        System.out.println("   allure serve target/allure-results");
        System.out.println("=======================================================\n");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getName());
        if (result.getThrowable() != null) {
            logger.error("Failure reason: " + result.getThrowable().getMessage());
        }
        
        // Dynamic screenshot attachment for Selenium UI runs
        try {
            WebDriver driver = DriverManager.getInstance().getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
                logger.info("Captured failure screenshot and attached to Allure");
            }
        } catch (Exception e) {
            logger.warn("No active WebDriver found or failed to capture screenshot: {}", e.getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: " + result.getName());
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: " + result.getName());
    }
}