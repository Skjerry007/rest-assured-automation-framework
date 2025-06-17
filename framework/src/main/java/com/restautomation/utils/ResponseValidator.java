package com.restautomation.utils;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


/**
 * ResponseValidator - Utility for validating API responses
 */
public class ResponseValidator {
    
    private ResponseValidator() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Validate response status code
     * @param response API response
     * @param expectedStatusCode expected status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        LoggerUtil.info("Validating status code: Expected {} | Actual {}", 
                expectedStatusCode, response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, 
                "Status code validation failed");
    }
    
    /**
     * Validate response contains a field
     * @param response API response
     * @param fieldPath JSON path to the field
     */
    public static void validateFieldExists(Response response, String fieldPath) {
        LoggerUtil.info("Validating field exists: {}", fieldPath);
        Assert.assertNotNull(response.jsonPath().get(fieldPath), 
                "Field " + fieldPath + " not found in response");
    }
    
    /**
     * Validate field value
     * @param response API response
     * @param fieldPath JSON path to the field
     * @param expectedValue expected value
     */
    public static void validateFieldValue(Response response, String fieldPath, Object expectedValue) {
        LoggerUtil.info("Validating field value: {} = {}", fieldPath, expectedValue);
        Object actualValue = response.jsonPath().get(fieldPath);
        Assert.assertEquals(actualValue, expectedValue, 
                "Field " + fieldPath + " value mismatch");
    }
    
    /**
     * Validate response time
     * @param response API response
     * @param maxTimeInMillis maximum allowed response time in milliseconds
     */
    public static void validateResponseTime(Response response, long maxTimeInMillis) {
        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        LoggerUtil.info("Response time: {} ms (max allowed: {} ms)", responseTime, maxTimeInMillis);
        Assert.assertTrue(responseTime <= maxTimeInMillis, 
                "Response time (" + responseTime + " ms) exceeds maximum allowed time (" + maxTimeInMillis + " ms)");
    }
    
    /**
     * Validate content type
     * @param response API response
     * @param expectedContentType expected content type
     */
    public static void validateContentType(Response response, String expectedContentType) {
        LoggerUtil.info("Validating content type: Expected {} | Actual {}", 
                expectedContentType, response.getContentType());
        Assert.assertEquals(response.getContentType(), expectedContentType, 
                "Content type validation failed");
    }
    
    /**
     * Validate response header
     * @param response API response
     * @param headerName header name
     * @param expectedValue expected header value
     */
    public static void validateHeader(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        LoggerUtil.info("Validating header {}: Expected {} | Actual {}", 
                headerName, expectedValue, actualValue);
        Assert.assertEquals(actualValue, expectedValue, 
                "Header " + headerName + " value mismatch");
    }
    
    /**
     * Validate response schema using JSON schema
     * @param response API response
     * @param schemaPath path to JSON schema file
     */
    public static void validateSchema(Response response, String schemaPath) {
    LoggerUtil.info("Validating response schema using: {}", schemaPath);
    
    if (schemaPath == null || schemaPath.trim().isEmpty()) {
        LoggerUtil.error("Schema path is null or empty.");
        Assert.fail("Schema path cannot be null or empty.");
    }

    try {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
        LoggerUtil.info("Schema validation passed");
    } catch (Exception e) {
        LoggerUtil.error("Schema validation failed: {}", e.getMessage());
        Assert.fail("Schema validation failed: " + e.getMessage());
    }
}
}