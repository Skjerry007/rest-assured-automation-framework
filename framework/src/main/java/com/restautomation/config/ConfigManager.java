package com.restautomation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager - Singleton class to manage configuration properties
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static ConfigManager instance;
    private static final String CONFIG_FILE = "src/test/resources/config/dev-config.properties";

    private ConfigManager() {
        loadConfig();
    }

    /**
     * Singleton getInstance method
     * @return ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            logger.info("Loading configuration from: {}", CONFIG_FILE);
            try (InputStream input = new FileInputStream(CONFIG_FILE)) {
                properties.load(input);
            }
        } catch (IOException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Get property value
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.error("Configuration property not found: {}", key);
            throw new RuntimeException("Configuration property not found: " + key);
        }
        return value;
    }
    
    /**
     * Get property value with default
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default
     */
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Configuration property not found: {}. Using default: {}", key, defaultValue);
            return defaultValue;
        }
        return value;
    }
    
    /**
     * Get base URL for the API
     * @return base URL
     */
    public String getBaseUrl() {
        return getProperty("baseUrl");
    }
    
    /**
     * Get timeout value for API requests
     * @return timeout in seconds
     */
    public int getTimeout() {
        try {
            return Integer.parseInt(getProperty("timeout", "30"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid timeout value, using default 30 seconds");
            return 30;
        }
    }
    
    /**
     * Check if SSL verification is enabled
     * @return true if SSL verification is enabled
     */
    public boolean isSslVerificationEnabled() {
        return Boolean.parseBoolean(getProperty("api.sslVerification", "true"));
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        logger.info("Set configuration property: {} = {}", key, value);
    }

    public String getApiKey() {
        return getProperty("apiKey");
    }
}