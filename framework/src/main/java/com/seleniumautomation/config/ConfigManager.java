package com.seleniumautomation.config;

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

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.error("Configuration property not found: {}", key);
            throw new RuntimeException("Configuration property not found: " + key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Configuration property not found: {}. Using default: {}", key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        logger.info("Set configuration property: {} = {}", key, value);
    }

    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
    
    public String getBaseUrl() {
        return getProperty("baseUrl");
    }
    
    public int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicitWait", "30"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit wait value, using default 30 seconds");
            return 30;
        }
    }
    
    public String getDbUrl() {
        return getProperty("db.url");
    }
    
    public String getDbUsername() {
        return getProperty("db.username");
    }
    
    public String getDbPassword() {
        return getProperty("db.password");
    }
}