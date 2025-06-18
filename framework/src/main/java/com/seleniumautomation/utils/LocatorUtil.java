package com.seleniumautomation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LocatorUtil {
    private static final Logger logger = LoggerFactory.getLogger(LocatorUtil.class);
    private static final Properties locatorProperties = new Properties();
    private static final String LOCATORS_PATH = "/com/seleniumautomation/locators/";

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

    // Self-healing locator: try to find a similar element if the locator fails
    public static By getOrHealLocator(WebDriver driver, String fileName, String locatorKey) {
        String locator = getLocator(fileName, locatorKey);
        if (locator == null) {
            logger.error("Locator not found for key: {}", locatorKey);
            throw new RuntimeException("Locator not found and could not be healed: " + locatorKey);
        }
        return By.xpath(locator);
    }

    // Generate a simple XPath for a WebElement
    private static String generateXpath(WebElement element) {
        String tag = element.getTagName();
        String id = element.getAttribute("id");
        if (id != null && !id.isEmpty()) {
            return "//" + tag + "[@id='" + id + "']";
        }
        String name = element.getAttribute("name");
        if (name != null && !name.isEmpty()) {
            return "//" + tag + "[@name='" + name + "']";
        }
        String classes = element.getAttribute("class");
        if (classes != null && !classes.isEmpty()) {
            return "//" + tag + "[contains(@class, '" + classes.split(" ")[0] + "')]";
        }
        return "//" + tag;
    }

    // Update the locator in the properties file
    private static void updateLocatorInProperties(String fileName, String key, String newXpath) {
        try {
            String relPath = "src/main/java/com/seleniumautomation/locators/" + fileName + ".properties";
            File file = new File(relPath);
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }
            props.setProperty(key, newXpath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, "Self-healed locator updated");
            }
        } catch (IOException e) {
            logger.error("Failed to update locator in properties file", e);
        }
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