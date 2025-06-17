package com.seleniumautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocatorUtil {
    private static final String LOCATORS_PATH = "/com/seleniumautomation/locators/";
    private static final Logger LOGGER = Logger.getLogger(LocatorUtil.class.getName());

    public static String getLocator(String fileName, String key) {
        Properties props = new Properties();
        String path = LOCATORS_PATH + fileName + ".properties";
        try (InputStream input = LocatorUtil.class.getResourceAsStream(path)) {
            if (input == null) {
                LOGGER.log(Level.WARNING, "Locator file not found on classpath: {0}. Trying file system fallback.", path);
                // Fallback: try loading from file system (src/test/resources)
                String fsPath = "src/test/resources" + path;
                File fsFile = new File(fsPath);
                if (!fsFile.exists()) {
                    throw new RuntimeException("Locator file not found on classpath or file system: " + path + " | " + fsPath);
                }
                try (FileInputStream fis = new FileInputStream(fsFile)) {
                    props.load(fis);
                }
            } else {
                props.load(input);
            }
            String value = props.getProperty(key);
            if (value == null) {
                throw new RuntimeException("Locator key not found: " + key + " in file: " + path);
            }
            LOGGER.log(Level.INFO, "Loaded locator for key {0} from {1}: {2}", new Object[]{key, path, value});
            return value;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load locator file: " + path, e);
        }
    }

    // Self-healing locator: try to find a similar element if the locator fails
    public static By getOrHealLocator(WebDriver driver, String fileName, String key) {
        String locator = getLocator(fileName, key);
        try {
            driver.findElement(By.xpath(locator));
            return By.xpath(locator);
        } catch (NoSuchElementException e) {
            // Try to heal: look for similar elements
            List<WebElement> candidates = driver.findElements(By.xpath("//*[contains(@id, '" + key.toLowerCase().replace("_", "") + "') or contains(@name, '" + key.toLowerCase().replace("_", "") + "') or contains(@class, '" + key.toLowerCase().replace("_", "") + "') or contains(text(), '" + key.replace("_", " ") + "') ]"));
            if (!candidates.isEmpty()) {
                String newXpath = generateXpath(candidates.get(0));
                updateLocatorInProperties(fileName, key, newXpath);
                LOGGER.log(Level.INFO, "Self-healed locator for {0}: {1}", new Object[]{key, newXpath});
                return By.xpath(newXpath);
            } else {
                throw new RuntimeException("Locator not found and could not be healed: " + key);
            }
        }
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
            LOGGER.log(Level.SEVERE, "Failed to update locator in properties file", e);
        }
    }
} 