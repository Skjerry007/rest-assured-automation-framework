package com.seleniumautomation.utils;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.restautomation.utils.LoggerUtil;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LocatorUtil {
    private static final Logger logger = LoggerFactory.getLogger(LocatorUtil.class);
    private static final Properties locatorProperties = new Properties();
    private static final Map<String, String> healedLocators = new HashMap<>();
    private static final Map<String, List<String>> locatorHistory = new HashMap<>();

    private static final boolean AUTO_UPDATE_PROPERTIES = true;
    private static final boolean ENABLE_LEARNING = true;

    // Load locators from .properties file
    public static void loadLocators(String fileName) throws IOException {
        String resourcePath = "com/seleniumautomation/locators/" + fileName + ".properties";
        try (InputStream input = LocatorUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Could not find locator file: " + resourcePath);
            }
            locatorProperties.load(input);
        }
    }

    public static void loadPropertiesFromFile(String fileName) throws IOException {
        loadLocators(fileName); // delegate
    }

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

    // Single string-to-By parser (supports css=, id=, xpath=, etc.)
    public static By getByLocator(String locatorString) {
        return parseLocator(locatorString);
    }


    // Main locator parser (multiple fallback locator strings with `|` separator)
    public static List<By> getLocators(String key) {
        String value = locatorProperties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("No locator found for key: " + key);
        }
        return Arrays.stream(value.split("\\|"))
                .map(String::trim)
                .map(LocatorUtil::parseLocator)
                .collect(Collectors.toList());
    }

    private static By parseLocator(String locator) {
        if (locator == null) throw new IllegalArgumentException("Locator string cannot be null");
        locator = locator.trim();

        // Support both = and :
        if (locator.startsWith("xpath=") || locator.startsWith("xpath:")) return By.xpath(locator.substring(6));
        if (locator.startsWith("css=") || locator.startsWith("css:")) return By.cssSelector(locator.substring(4));
        if (locator.startsWith("id=")) return By.id(locator.substring(3));
        if (locator.startsWith("name=")) return By.name(locator.substring(5));
        if (locator.startsWith("class=")) return By.className(locator.substring(6));
        if (locator.startsWith("//")) return By.xpath(locator);
        if (locator.startsWith(".") || locator.startsWith("#")) return By.cssSelector(locator);

        if (!locator.contains(" ") && !locator.contains("[")) return By.id(locator);
        return By.cssSelector(locator);
    }

    // Alternative format like css: or xpath:


    // Self-healing with fallback and learning
    public static By advancedSelfHealing(WebDriver driver, String fileName, String locatorKey, String elementDescription) {
        String originalLocator = getLocator(fileName, locatorKey);
        List<By> locatorStrategies = new ArrayList<>();

        if (originalLocator != null) {
            locatorStrategies.add(parseLocator(originalLocator));
        }
        locatorStrategies.addAll(generateFallbackLocators(elementDescription));

        for (int i = 0; i < locatorStrategies.size(); i++) {
            By locator = locatorStrategies.get(i);
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    String workingLocator = locator.toString();
                    LoggerUtil.info("Self-healing successful for '{}' using locator #{}: {}", locatorKey, i + 1, workingLocator);
                    recordSuccessfulLocator(locatorKey, workingLocator, i == 0);

                    if (i > 0 && AUTO_UPDATE_PROPERTIES) {
                        updateLocatorInProperties(fileName, locatorKey, workingLocator);
                    }
                    return locator;
                }
            } catch (Exception e) {
                LoggerUtil.debug("Locator #{} failed for '{}': {}", i + 1, locatorKey, e.getMessage());
            }
        }

        if (ENABLE_LEARNING) {
            By learnedLocator = learnNewLocator(driver, elementDescription);
            if (learnedLocator != null) {
                String learnedLocatorStr = learnedLocator.toString();
                LoggerUtil.info("Learned new locator for '{}': {}", locatorKey, learnedLocatorStr);
                updateLocatorInProperties(fileName, locatorKey, learnedLocatorStr);
                return learnedLocator;
            }
        }

        throw new org.openqa.selenium.NoSuchElementException("All locator strategies failed for: " + locatorKey);
    }

    // Lightweight healing fallback
    public static By selfHealing(By... locators) {
        return new By() {
            @Override
            public List<WebElement> findElements(SearchContext context) {
                Exception lastException = null;

                for (int i = 0; i < locators.length; i++) {
                    try {
                        List<WebElement> elements = locators[i].findElements(context);
                        if (!elements.isEmpty()) {
                            LoggerUtil.info("Self-healing success using locator #{}: {}", i + 1, locators[i]);
                            return elements;
                        }
                    } catch (Exception e) {
                        lastException = e;
                        LoggerUtil.debug("Locator #{} failed: {}", i + 1, e.getMessage());
                    }
                }

                if (context instanceof WebDriver && ENABLE_LEARNING) {
                    By learnedLocator = learnNewLocatorFromContext((WebDriver) context, locators);
                    if (learnedLocator != null) {
                        return learnedLocator.findElements(context);
                    }
                }

                throw new org.openqa.selenium.NoSuchElementException("No locator matched", lastException);
            }
        };
    }

    private static List<By> generateFallbackLocators(String description) {
        List<By> fallbacks = new ArrayList<>();
        if (description == null || description.isEmpty()) return fallbacks;

        String d = description.toLowerCase();

        if (d.contains("button")) fallbacks.addAll(List.of(By.cssSelector("button"), By.xpath("//button")));
        if (d.contains("input") || d.contains("field")) fallbacks.addAll(List.of(By.cssSelector("input"), By.xpath("//input")));
        if (d.contains("link")) fallbacks.addAll(List.of(By.cssSelector("a"), By.xpath("//a")));
        if (d.contains("cart")) fallbacks.addAll(List.of(By.cssSelector(".cart"), By.xpath("//*[contains(@class,'cart')]")));

        fallbacks.addAll(List.of(
            By.cssSelector("[data-test]"),
            By.xpath("//*[@data-test]"),
            By.cssSelector("[id]"),
            By.xpath("//*[@id]")
        ));

        return fallbacks;
    }

    private static By learnNewLocator(WebDriver driver, String description) {
        try {
            List<WebElement> candidates = driver.findElements(By.cssSelector("*"));
            for (WebElement el : candidates) {
                if (el.isDisplayed()) {
                    return generateRobustLocator(el);
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error learning new locator: {}", e.getMessage());
        }
        return null;
    }

    private static By learnNewLocatorFromContext(WebDriver driver, By[] failedLocators) {
        try {
            for (WebElement el : driver.findElements(By.cssSelector("*"))) {
                if (el.isDisplayed()) {
                    return generateRobustLocator(el);
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error learning from context: {}", e.getMessage());
        }
        return null;
    }

    private static By generateRobustLocator(WebElement element) {
        try {
            if (element.getAttribute("id") != null) return By.id(element.getAttribute("id"));
            if (element.getAttribute("data-test") != null)
                return By.cssSelector("[data-test='" + element.getAttribute("data-test") + "']");
            if (element.getAttribute("class") != null)
                return By.cssSelector("." + element.getAttribute("class").split(" ")[0]);

            return By.cssSelector(element.getTagName());
        } catch (Exception e) {
            LoggerUtil.error("Error generating locator: {}", e.getMessage());
            return null;
        }
    }

    private static void updateLocatorInProperties(String fileName, String key, String newLocator) {
        try {
            String relPath = "src/main/java/com/seleniumautomation/locators/" + fileName + ".properties";
            File file = new File(relPath);
            if (!file.exists()) return;

            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }

            props.setProperty(key, newLocator);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, "Auto-healed update");
            }

            LoggerUtil.info("Updated '{}' in {} to {}", key, fileName, newLocator);
        } catch (IOException e) {
            LoggerUtil.error("Error updating locator in file: {}", e.getMessage());
        }
    }

    private static void recordSuccessfulLocator(String key, String locator, boolean wasOriginal) {
        healedLocators.put(key, locator);
        locatorHistory.computeIfAbsent(key, k -> new ArrayList<>()).add(locator);
        if (!wasOriginal) {
            LoggerUtil.info("Recorded healed locator for '{}': {}", key, locator);
        }
    }

    public static Map<String, Object> getHealingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("healedLocators", new HashMap<>(healedLocators));
        stats.put("locatorHistory", new HashMap<>(locatorHistory));
        stats.put("totalHealed", healedLocators.size());
        stats.put("autoUpdateEnabled", AUTO_UPDATE_PROPERTIES);
        stats.put("learningEnabled", ENABLE_LEARNING);
        return stats;
    }

    public static void clearHealingCache() {
        healedLocators.clear();
        locatorHistory.clear();
        LoggerUtil.info("Healing cache cleared");
    }
}
