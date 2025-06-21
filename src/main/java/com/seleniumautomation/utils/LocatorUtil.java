package com.seleniumautomation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.restautomation.utils.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LocatorUtil {
    private static final Logger logger = LoggerFactory.getLogger(LocatorUtil.class);
    private static final Properties locatorProperties = new Properties();
    private static final String LOCATORS_PATH = "/com/seleniumautomation/locators/";
    private static final Map<String, String> healedLocators = new HashMap<>();
    private static final Map<String, List<String>> locatorHistory = new HashMap<>();
    
    // Configuration for self-healing
    private static final int MAX_HEALING_ATTEMPTS = 5;
    private static final boolean AUTO_UPDATE_PROPERTIES = true;
    private static final boolean ENABLE_LEARNING = true;

    public static String getLocator(String fileName, String locatorKey) {
        try {
            if (!locatorProperties.containsKey(locatorKey)) {
                loadLocators(fileName);
            }
            String locator = locatorProperties.getProperty(locatorKey);
            logger.info("Loaded locator for key {} from {}: {}", locatorKey, fileName, locator);
            return locator;
        } catch (Exception e) {
            logger.error("Failed to get locator for key {}: {}", locatorKey, e.getMessage());
            return null;
        }
    }

    /**
     * Advanced self-healing locator with automatic updates and learning
     */
    public static By advancedSelfHealing(WebDriver driver, String fileName, String locatorKey, String elementDescription) {
        String originalLocator = getLocator(fileName, locatorKey);
        List<By> locatorStrategies = new ArrayList<>();
        
        // Add original locator if available
        if (originalLocator != null) {
            locatorStrategies.add(parseLocator(originalLocator));
        }
        
        // Add common fallback strategies
        locatorStrategies.addAll(generateFallbackLocators(elementDescription));
        
        // Try each locator strategy
        for (int i = 0; i < locatorStrategies.size(); i++) {
            By locator = locatorStrategies.get(i);
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    String workingLocator = locator.toString();
                    LoggerUtil.info("Self-healing successful for '{}' using locator #{}: {}", 
                                  locatorKey, i + 1, workingLocator);
                    
                    // Record successful locator
                    recordSuccessfulLocator(locatorKey, workingLocator, i == 0);
                    
                    // Auto-update if this is a new working locator
                    if (i > 0 && AUTO_UPDATE_PROPERTIES) {
                        updateLocatorInProperties(fileName, locatorKey, workingLocator);
                        LoggerUtil.info("Auto-updated locator for '{}' in properties file", locatorKey);
                    }
                    
                    return locator;
                }
            } catch (Exception e) {
                LoggerUtil.debug("Locator strategy #{} failed for '{}': {}", i + 1, locatorKey, e.getMessage());
            }
        }
        
        // If all strategies fail, try to learn new locator
        if (ENABLE_LEARNING) {
            By learnedLocator = learnNewLocator(driver, elementDescription);
            if (learnedLocator != null) {
                String learnedLocatorStr = learnedLocator.toString();
                LoggerUtil.info("Learned new locator for '{}': {}", locatorKey, learnedLocatorStr);
                updateLocatorInProperties(fileName, locatorKey, learnedLocatorStr);
                return learnedLocator;
            }
        }
        
        throw new NoSuchElementException("All locator strategies failed for: " + locatorKey);
    }

    /**
     * Enhanced self-healing with multiple locator strategies
     */
    public static By selfHealing(By... locators) {
        return new By() {
            @Override
            public List<WebElement> findElements(SearchContext context) {
                Exception lastException = null;
                
                for (int i = 0; i < locators.length; i++) {
                    try {
                        List<WebElement> elements = locators[i].findElements(context);
                        if (!elements.isEmpty()) {
                            LoggerUtil.info("Self-healing successful using locator #{}: {}", i + 1, locators[i]);
                            return elements;
                        }
                    } catch (Exception e) {
                        lastException = e;
                        LoggerUtil.debug("Locator #{} failed: {}", i + 1, e.getMessage());
                    }
                }
                
                // If all provided locators fail, try to generate new ones
                if (context instanceof WebDriver && ENABLE_LEARNING) {
                    WebDriver driver = (WebDriver) context;
                    By learnedLocator = learnNewLocatorFromContext(driver, locators);
                    if (learnedLocator != null) {
                        try {
                            List<WebElement> elements = learnedLocator.findElements(context);
                            if (!elements.isEmpty()) {
                                LoggerUtil.info("Learned new locator successful: {}", learnedLocator);
                                return elements;
                            }
                        } catch (Exception e) {
                            LoggerUtil.debug("Learned locator also failed: {}", e.getMessage());
                        }
                    }
                }
                
                throw new NoSuchElementException("No locator matched", lastException);
            }
        };
    }

    /**
     * Generate fallback locators based on element description
     */
    private static List<By> generateFallbackLocators(String elementDescription) {
        List<By> fallbacks = new ArrayList<>();
        
        if (elementDescription == null || elementDescription.isEmpty()) {
            return fallbacks;
        }
        
        String description = elementDescription.toLowerCase();
        
        // Generate locators based on common patterns
        if (description.contains("button")) {
            fallbacks.addAll(Arrays.asList(
                By.cssSelector("button"),
                By.xpath("//button"),
                By.cssSelector("[role='button']"),
                By.xpath("//*[@role='button']")
            ));
        }
        
        if (description.contains("input") || description.contains("field")) {
            fallbacks.addAll(Arrays.asList(
                By.cssSelector("input"),
                By.xpath("//input"),
                By.cssSelector("textarea"),
                By.xpath("//textarea")
            ));
        }
        
        if (description.contains("link") || description.contains("a")) {
            fallbacks.addAll(Arrays.asList(
                By.cssSelector("a"),
                By.xpath("//a"),
                By.cssSelector("[role='link']"),
                By.xpath("//*[@role='link']")
            ));
        }
        
        if (description.contains("cart")) {
            fallbacks.addAll(Arrays.asList(
                By.cssSelector(".cart"),
                By.cssSelector("[class*='cart']"),
                By.xpath("//*[contains(@class,'cart')]"),
                By.cssSelector("[data-test*='cart']"),
                By.xpath("//*[contains(@data-test,'cart')]")
            ));
        }
        
        if (description.contains("checkout")) {
            fallbacks.addAll(Arrays.asList(
                By.cssSelector(".checkout"),
                By.cssSelector("[class*='checkout']"),
                By.xpath("//*[contains(@class,'checkout')]"),
                By.cssSelector("[data-test*='checkout']"),
                By.xpath("//*[contains(@data-test,'checkout')]")
            ));
        }
        
        // Add generic fallbacks
        fallbacks.addAll(Arrays.asList(
            By.cssSelector("[data-test]"),
            By.xpath("//*[@data-test]"),
            By.cssSelector("[id]"),
            By.xpath("//*[@id]"),
            By.cssSelector("[class]"),
            By.xpath("//*[@class]")
        ));
        
        return fallbacks;
    }

    /**
     * Learn new locator by analyzing the page
     */
    private static By learnNewLocator(WebDriver driver, String elementDescription) {
        try {
            String description = elementDescription.toLowerCase();
            List<WebElement> candidates = new ArrayList<>();
            
            // Find elements by text content
            if (description.contains("button")) {
                candidates.addAll(driver.findElements(By.cssSelector("button")));
            }
            if (description.contains("input")) {
                candidates.addAll(driver.findElements(By.cssSelector("input")));
            }
            if (description.contains("link")) {
                candidates.addAll(driver.findElements(By.cssSelector("a")));
            }
            
            // Find elements by common attributes
            candidates.addAll(driver.findElements(By.cssSelector("[data-test]")));
            candidates.addAll(driver.findElements(By.cssSelector("[id]")));
            candidates.addAll(driver.findElements(By.cssSelector("[class]")));
            
            // Generate locator for the first visible element
            for (WebElement element : candidates) {
                if (element.isDisplayed()) {
                    return generateRobustLocator(element);
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.error("Error learning new locator: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * Learn new locator from context when all provided locators fail
     */
    private static By learnNewLocatorFromContext(WebDriver driver, By[] failedLocators) {
        try {
            // Try to find any visible element that might be what we're looking for
            List<WebElement> allElements = driver.findElements(By.cssSelector("*"));
            
            for (WebElement element : allElements) {
                if (element.isDisplayed() && element.isEnabled()) {
                    // Check if this element has any of the common attributes
                    String id = element.getAttribute("id");
                    String className = element.getAttribute("class");
                    String dataTest = element.getAttribute("data-test");
                    
                    if (id != null && !id.isEmpty()) {
                        return By.id(id);
                    }
                    if (dataTest != null && !dataTest.isEmpty()) {
                        return By.cssSelector("[data-test='" + dataTest + "']");
                    }
                    if (className != null && !className.isEmpty()) {
                        return By.cssSelector("." + className.split(" ")[0]);
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error learning locator from context: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * Generate a robust locator for an element
     */
    private static By generateRobustLocator(WebElement element) {
        try {
            // Priority order: id > data-test > name > class > tag
            String id = element.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                return By.id(id);
            }
            
            String dataTest = element.getAttribute("data-test");
            if (dataTest != null && !dataTest.isEmpty()) {
                return By.cssSelector("[data-test='" + dataTest + "']");
            }
            
            String name = element.getAttribute("name");
            if (name != null && !name.isEmpty()) {
                return By.name(name);
            }
            
            String className = element.getAttribute("class");
            if (className != null && !className.isEmpty()) {
                String firstClass = className.split(" ")[0];
                return By.cssSelector("." + firstClass);
            }
            
            // Fallback to tag name with position
            String tagName = element.getTagName();
            return By.cssSelector(tagName);
            
        } catch (Exception e) {
            LoggerUtil.error("Error generating robust locator: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parse locator string into By object
     */
    private static By parseLocator(String locatorString) {
        if (locatorString.startsWith("//")) {
            return By.xpath(locatorString);
        } else if (locatorString.startsWith(".")) {
            return By.cssSelector(locatorString);
        } else if (locatorString.startsWith("#")) {
            return By.cssSelector(locatorString);
        } else if (locatorString.startsWith("id=")) {
            return By.id(locatorString.substring(3));
        } else if (locatorString.startsWith("name=")) {
            return By.name(locatorString.substring(5));
        } else if (locatorString.startsWith("class=")) {
            return By.className(locatorString.substring(6));
        } else {
            // Default to CSS selector
            return By.cssSelector(locatorString);
        }
    }

    /**
     * Record successful locator for future reference
     */
    private static void recordSuccessfulLocator(String locatorKey, String locator, boolean wasOriginal) {
        healedLocators.put(locatorKey, locator);
        
        if (!locatorHistory.containsKey(locatorKey)) {
            locatorHistory.put(locatorKey, new ArrayList<>());
        }
        locatorHistory.get(locatorKey).add(locator);
        
        if (!wasOriginal) {
            LoggerUtil.info("Recorded new working locator for '{}': {}", locatorKey, locator);
        }
    }

    /**
     * Update the locator in the properties file
     */
    private static void updateLocatorInProperties(String fileName, String key, String newLocator) {
        try {
            String relPath = "src/main/java/com/seleniumautomation/locators/" + fileName + ".properties";
            File file = new File(relPath);
            
            if (!file.exists()) {
                LoggerUtil.warn("Properties file does not exist: {}", relPath);
                return;
            }
            
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }
            
            String oldLocator = props.getProperty(key);
            props.setProperty(key, newLocator);
            
            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, "Self-healed locator updated from '" + oldLocator + "' to '" + newLocator + "'");
            }
            
            LoggerUtil.info("Successfully updated locator '{}' in {}: {} -> {}", key, fileName, oldLocator, newLocator);
            
        } catch (IOException e) {
            LoggerUtil.error("Failed to update locator in properties file: {}", e.getMessage());
        }
    }

    /**
     * Get healing statistics
     */
    public static Map<String, Object> getHealingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("healedLocators", new HashMap<>(healedLocators));
        stats.put("locatorHistory", new HashMap<>(locatorHistory));
        stats.put("totalHealed", healedLocators.size());
        stats.put("autoUpdateEnabled", AUTO_UPDATE_PROPERTIES);
        stats.put("learningEnabled", ENABLE_LEARNING);
        return stats;
    }

    /**
     * Clear healing cache
     */
    public static void clearHealingCache() {
        healedLocators.clear();
        locatorHistory.clear();
        LoggerUtil.info("Healing cache cleared");
    }

    private static void loadLocators(String fileName) throws IOException {
        String resourcePath = "com/seleniumautomation/locators/" + fileName + ".properties";
        try (InputStream input = LocatorUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Could not find locator file: " + resourcePath);
            }
            locatorProperties.load(input);
        }
    }
} 