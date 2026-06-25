package common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager - Singleton class to manage configuration properties for both API and UI tests.
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static ConfigManager instance;
    private static final String CONFIG_FILE = "src/test/resources/config/config.properties";

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

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        logger.info("Set configuration property: {} = {}", key, value);
    }

    // ==========================================
    // API Configuration Getters
    // ==========================================
    
    public String getBaseUrl() {
        return getProperty("api.baseUrl");
    }
    
    public int getTimeout() {
        try {
            return Integer.parseInt(getProperty("timeout", "30"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid timeout value, using default 30 seconds");
            return 30;
        }
    }
    
    public boolean isSslVerificationEnabled() {
        return Boolean.parseBoolean(getProperty("api.sslVerification", "true"));
    }

    public String getApiKey() {
        return getProperty("apiKey");
    }

    // ==========================================
    // UI (Selenium) Configuration Getters
    // ==========================================

    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public String getPlatform() {
        return getProperty("platform", "dweb");
    }

    public String getWebUrl() {
        String platform = getPlatform().toLowerCase();
        return getProperty("web.url." + platform);
    }
    
    public String getNaukriEmail() {
        return System.getProperty("naukri.email", getProperty("naukri.email", ""));
    }

    public String getNaukriPassword() {
        return System.getProperty("naukri.password", getProperty("naukri.password", ""));
    }

    public String getResumePath() {
        return System.getProperty("naukri.resumePath", getProperty("naukri.resumePath", "src/test/resources/resume.pdf"));
    }
    
    public int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicitWait", "30"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit wait value, using default 30 seconds");
            return 30;
        }
    }

    // ==========================================
    // DB Configuration Getters
    // ==========================================
    
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
