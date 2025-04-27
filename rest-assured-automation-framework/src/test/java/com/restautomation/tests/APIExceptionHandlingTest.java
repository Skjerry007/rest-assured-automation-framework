package com.restautomation.tests;

import com.restautomation.base.BaseAPI;
import com.restautomation.exceptions.APIException;
import com.restautomation.utils.LoggerUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * APIExceptionHandlingTest - Tests for API exception handling
 */
public class APIExceptionHandlingTest {
    
    /**
     * Test handling connection timeout
     */
    @Test(description = "Test connection timeout handling")
    public void testConnectionTimeout() {
        LoggerUtil.info("Testing connection timeout handling");
        
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://10.255.255.1") // Non-routable IP address to force timeout
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 1000) // 1 second timeout
                                .setParam("http.socket.timeout", 1000)))
                .build();
        
        try {
            Response response = requestSpec.when().get("/");
            Assert.fail("Expected exception was not thrown");
        } catch (Exception e) {
            LoggerUtil.info("Caught expected exception: {}", e.getMessage());
            
            // Verify that the exception is either ConnectException or SocketTimeoutException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConnectException || cause instanceof SocketTimeoutException) {
                    LoggerUtil.info("Successfully caught connection timeout exception: {}", cause.getClass().getName());
                    return;
                }
                cause = cause.getCause();
            }
            
            // If we didn't find the expected exception type
            Assert.fail("Expected ConnectException or SocketTimeoutException, but got: " + e.getClass().getName());
        }
    }
    
    /**
     * Test custom exception with status code
     */
    @Test(description = "Test custom API exception with status code")
    public void testCustomAPIException() {
        LoggerUtil.info("Testing custom API exception");
        
        try {
            // Simulate API error
            throw new APIException("Resource not found", 404);
        } catch (APIException e) {
            LoggerUtil.info("Caught API exception: {}, Status Code: {}", e.getMessage(), e.getStatusCode());
            Assert.assertEquals(e.getStatusCode(), 404, "Status code should be 404");
            Assert.assertEquals(e.getMessage(), "Resource not found", "Error message should match");
        }
    }
    
    /**
     * Test custom exception with nested cause
     */
    @Test(description = "Test custom API exception with nested cause")
    public void testCustomAPIExceptionWithCause() {
        LoggerUtil.info("Testing custom API exception with nested cause");
        
        try {
            // Simulate API error with cause
            throw new APIException("Failed to parse response", 
                    new IllegalArgumentException("Invalid JSON format"));
        } catch (APIException e) {
            LoggerUtil.info("Caught API exception: {}, Cause: {}", 
                    e.getMessage(), e.getCause().getMessage());
            Assert.assertEquals(e.getMessage(), "Failed to parse response", "Error message should match");
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException, 
                    "Cause should be IllegalArgumentException");
        }
    }
    
    /**
     * Test retry mechanism for transient errors
     */
    @Test(description = "Test retry mechanism for transient errors")
    public void testRetryMechanism() {
        LoggerUtil.info("Testing retry mechanism for transient errors");
        
        int maxRetries = 3;
        int retryCount = 0;
        boolean success = false;
        
        while (retryCount < maxRetries && !success) {
            try {
                // Simulate transient error on first two attempts
                if (retryCount < 2) {
                    LoggerUtil.info("Attempt {}: Simulating transient error", retryCount + 1);
                    throw new APIException("Service temporarily unavailable", 503);
                } else {
                    LoggerUtil.info("Attempt {}: Success", retryCount + 1);
                    success = true;
                }
            } catch (APIException e) {
                if (e.getStatusCode() == 503) {
                    retryCount++;
                    LoggerUtil.info("Retrying after transient error: {} (Attempt {})", 
                            e.getMessage(), retryCount);
                    
                    try {
                        // Exponential backoff
                        Thread.sleep(1000 * (long)Math.pow(2, retryCount - 1));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        LoggerUtil.error("Retry interrupted", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
        
        Assert.assertTrue(success, "Operation should succeed after retries");
        Assert.assertEquals(retryCount, 2, "Should have retried twice");
    }
}