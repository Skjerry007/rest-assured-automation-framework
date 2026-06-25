package common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestDataUtil - Utility for managing test data
 */
public class TestDataUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    
    // Thread-safe cache to avoid reading from disk repeatedly
    private static final Map<String, JsonNode> jsonCache = new ConcurrentHashMap<>();
    
    private TestDataUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Load test data from JSON file (cached)
     * @param fileName JSON file name
     * @return JsonNode with test data
     */
    public static JsonNode loadTestData(String fileName) {
        return jsonCache.computeIfAbsent(fileName, file -> {
            try {
                String filePath = TEST_DATA_DIR + file;
                LoggerUtil.info("Loading and caching test data from: {}", filePath);
                return objectMapper.readTree(new File(filePath));
            } catch (IOException e) {
                LoggerUtil.error("Error loading test data: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to load test data", e);
            }
        });
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
     * Get test data and deserialize directly to target class
     * @param fileName JSON file name
     * @param testName test name/key in the JSON
     * @param clazz Target class for deserialization
     * @param <T> Target class type
     * @return Deserialized object instance
     */
    public static <T> T getTestData(String fileName, String testName, Class<T> clazz) {
        JsonNode dataNode = getTestData(fileName, testName);
        try {
            return objectMapper.treeToValue(dataNode, clazz);
        } catch (IOException e) {
            LoggerUtil.error("Error mapping JSON node to class {}: {}", clazz.getName(), e.getMessage());
            throw new RuntimeException("Failed to map test data to object", e);
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

    /**
     * Clear the JSON cache
     */
    public static void clearCache() {
        jsonCache.clear();
        LoggerUtil.info("Cleared test data cache");
    }
}
