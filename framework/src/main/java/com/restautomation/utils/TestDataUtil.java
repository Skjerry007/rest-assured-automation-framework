package com.restautomation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * TestDataUtil - Utility for managing test data
 */
@Log4j2
public class TestDataUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    
    private TestDataUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Load test data from JSON file
     * @param fileName JSON file name
     * @return JsonNode with test data
     */
    public static JsonNode loadTestData(String fileName) {
        try {
            String filePath = TEST_DATA_DIR + fileName;
            LoggerUtil.info("Loading test data from: {}", filePath);
            return objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            LoggerUtil.error("Error loading test data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    /**
     * Get test data for a specific test
     * @param fileName JSON file name
     * @param testName test name/key in the JSON
     * @return JsonNode with test data for specified test
     */
    public static JsonNode getTestData(String fileName, String testName) {
        JsonNode rootNode = loadTestData(fileName);
        if (rootNode.has(testName)) {
            return rootNode.get(testName);
        } else {
            LoggerUtil.error("Test data not found for test: {} in file: {}", testName, fileName);
            throw new RuntimeException("Test data not found for test: " + testName);
        }
    }
    
    /**
     * Convert JSON to Map
     * @param jsonNode JSON node
     * @return Map representation of JSON
     */
    public static Map<String, Object> convertJsonToMap(JsonNode jsonNode) {
        try {
            return objectMapper.convertValue(jsonNode, HashMap.class);
        } catch (Exception e) {
            LoggerUtil.error("Error converting JSON to Map: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert JSON to Map", e);
        }
    }
    
    /**
     * Load JSON from file as string
     * @param fileName JSON file name
     * @return JSON string
     */
    public static String loadJsonFileAsString(String fileName) {
        try {
            String filePath = TEST_DATA_DIR + fileName;
            LoggerUtil.info("Loading JSON file as string: {}", filePath);
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LoggerUtil.error("Error loading JSON file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load JSON file", e);
        }
    }
    
    /**
     * Convert object to JSON string
     * @param object object to convert
     * @return JSON string
     */
    public static String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            LoggerUtil.error("Error converting object to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object
     * @param json JSON string
     * @param clazz object class
     * @param <T> object type
     * @return object instance
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LoggerUtil.error("Error converting JSON to object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
}