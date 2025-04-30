package com.seleniumautomation.config;

import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager - Singleton class to manage configuration properties
 */
@Log4j2
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        properties = new Properties();
        try {
            String env = System.getProperty("env", "qa");
            String configFile = "src/test/resources/config/" + env + "-config.properties";
            
            log.info("Loading configuration from: {}", configFile);
            properties.load(new FileInputStream(configFile));
        } catch (IOException e) {
            log.error("Error loading config properties: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load config properties", e);
        }
    }
    
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property {} not found in configuration", key);
        }
        return value;
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
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
            log.warn("Invalid explicit wait value, using default 30 seconds");
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