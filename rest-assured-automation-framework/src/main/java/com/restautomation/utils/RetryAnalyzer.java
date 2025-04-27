package com.restautomation.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Retry failed tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            LoggerUtil.info("Retrying test: {} for the {} time", result.getName(), retryCount + 1);
            retryCount++;
            return true;
        }
        LoggerUtil.info("Test: {} has reached max retry count: {}", result.getName(), MAX_RETRY_COUNT);
        return false;
    }
}