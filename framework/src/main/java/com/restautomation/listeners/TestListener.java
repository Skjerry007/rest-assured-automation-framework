package com.restautomation.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.restautomation.reports.ExtentReportManager;
import com.restautomation.utils.LoggerUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestListener - TestNG listener for test execution events
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: " + context.getName());
        // Initialize the extent report
        ExtentReportManager.initReports();
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: " + context.getName());
        // Flush the extent report
        ExtentReportManager.flushReports();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getName());
        // Create a test for the current test method
        ExtentTest test = ExtentReportManager.createTest(result.getName(), 
                result.getTestClass().getRealClass().getSimpleName());
        
        // Log test description if available
        if (result.getMethod().getDescription() != null && !result.getMethod().getDescription().isEmpty()) {
            test.info(result.getMethod().getDescription());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getName());
        // Log the successful test
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.PASS, MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
        
        // Add execution time
        test.info("Test execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed", ExtentColor.RED));

        if (result.getThrowable() != null) {
            test.log(Status.FAIL, result.getThrowable());
            String stackTrace = exceptionStackTraceToString(result.getThrowable());
            // Use string instead of enum for language
            test.log(Status.FAIL, MarkupHelper.createCodeBlock(stackTrace, "java"));
            logger.error("Failure reason: " + result.getThrowable().getMessage());
        }

        test.info("Test execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: " + result.getName());
        // Log the skipped test
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
        
        // Log the exception if available
        if (result.getThrowable() != null) {
            test.log(Status.SKIP, result.getThrowable());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: " + result.getName());
    }
    
    /**
     * Convert exception stack trace to string
     * @param throwable exception
     * @return stack trace as string
     */
    private String exceptionStackTraceToString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        return sb.toString();
    }
}