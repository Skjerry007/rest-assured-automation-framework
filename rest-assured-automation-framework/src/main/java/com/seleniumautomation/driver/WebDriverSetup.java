package com.seleniumautomation.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.seleniumautomation.config.ConfigManager;
import com.restautomation.utils.LoggerUtil;

public class WebDriverSetup {
    private static final String GRID_HUB_URL = "http://localhost:4444/wd/hub";
    private static final String PDF_UPLOAD_DIR = "/usr/uploads";
    
    public static WebDriver setupDriver() {
        String browser = ConfigManager.getInstance().getBrowser().toLowerCase();
        boolean isGrid = Integer.parseInt(System.getProperty("gridExecutorCapacity", "0")) > 0;
        
        try {
            if (isGrid) {
                return setupGridDriver(browser);
            } else {
                return setupLocalDriver(browser);
            }
        } catch (Exception e) {
            LoggerUtil.error("Error setting up WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to setup WebDriver", e);
        }
    }
    
    private static WebDriver setupLocalDriver(String browser) {
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (ConfigManager.getInstance().isHeadless()) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                
                // Set download directory for PDFs
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", PDF_UPLOAD_DIR);
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (ConfigManager.getInstance().isHeadless()) {
                    firefoxOptions.addArguments("--headless");
                }
                return new FirefoxDriver(firefoxOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    
    private static WebDriver setupGridDriver(String browser) {
        try {
            URL hubUrl = new URL(GRID_HUB_URL);
            
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    // Configure Chrome for Grid
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    
                    // Set up PDF handling in Grid
                    Map<String, Object> prefs = new HashMap<>();
                    prefs.put("download.default_directory", PDF_UPLOAD_DIR);
                    chromeOptions.setExperimentalOption("prefs", prefs);
                    
                    return new RemoteWebDriver(hubUrl, chromeOptions);
                    
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    // Configure Firefox for Grid
                    return new RemoteWebDriver(hubUrl, firefoxOptions);
                    
                default:
                    throw new IllegalArgumentException("Unsupported browser for Grid: " + browser);
            }
        } catch (Exception e) {
            LoggerUtil.error("Error setting up Grid WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to setup Grid WebDriver", e);
        }
    }
}