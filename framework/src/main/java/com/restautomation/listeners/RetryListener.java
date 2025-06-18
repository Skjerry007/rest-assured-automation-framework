package com.restautomation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RetryListener implements IRetryAnalyzer, ITestNGListener {
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.info("Retrying test: " + result.getName() + " for the " + retryCount + " time");
            return true;
        }
        return false;
    }
} 