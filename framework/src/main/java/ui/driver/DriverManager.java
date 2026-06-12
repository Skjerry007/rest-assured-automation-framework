package ui.driver;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.JavascriptExecutor;

import common.config.ConfigManager;
import common.utils.LoggerUtil;

/**
 * DriverManager - Singleton class to manage WebDriver instances
 */
public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static DriverManager instance = null;
    
    private DriverManager() {
        // Private constructor
    }
    
    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }
    
    public WebDriver getDriver() {
        return driver.get();
    }
    
    public void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }
    
    public void initializeDriver() {
        WebDriver webDriver = null;
        String browser = ConfigManager.getInstance().getBrowser().toLowerCase();
        String platform = ConfigManager.getInstance().getPlatform().toLowerCase();
        
        try {
            switch (browser) {
                case "chrome":

                    ChromeOptions chromeOptions = new ChromeOptions();
                    
                    // Anti-bot detection measures for Udemy
                    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-plugins");
                    chromeOptions.addArguments("--disable-images");
                    chromeOptions.addArguments("--disable-javascript-harmony-shipping");
                    chromeOptions.addArguments("--disable-background-timer-throttling");
                    chromeOptions.addArguments("--disable-backgrounding-occluded-windows");
                    chromeOptions.addArguments("--disable-renderer-backgrounding");
                    chromeOptions.addArguments("--disable-features=TranslateUI");
                    chromeOptions.addArguments("--disable-ipc-flooding-protection");
                    
                    // User agent spoofing - use a real browser user agent if not mobile
                    if ("mweb".equals(platform)) {
                        java.util.Map<String, String> mobileEmulation = new java.util.HashMap<>();
                        mobileEmulation.put("deviceName", "Nexus 5");
                        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                    } else {
                        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                    }
                    
                    // Additional stealth settings
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    chromeOptions.setExperimentalOption("useAutomationExtension", false);
                    
                    if (ConfigManager.getInstance().isHeadless()) {
                        chromeOptions.addArguments("--headless");
                    }
                    if (!"mweb".equals(platform)) {
                        chromeOptions.addArguments("--start-maximized");
                    }
                    chromeOptions.addArguments("--disable-notifications");
                    webDriver = new ChromeDriver(chromeOptions);
                    
                    // Execute stealth script to remove automation indicators
                    ((JavascriptExecutor) webDriver).executeScript(
                        "Object.defineProperty(navigator, 'webdriver', {get: () => undefined});"
                    );
                    break;
                    
                case "firefox":

                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (ConfigManager.getInstance().isHeadless()) {
                        firefoxOptions.addArguments("--headless");
                    }
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;
                    
                case "edge":

                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (ConfigManager.getInstance().isHeadless()) {
                        edgeOptions.addArguments("--headless");
                    }
                    webDriver = new EdgeDriver(edgeOptions);
                    break;
                    
                default:
                    LoggerUtil.error("Unsupported browser: {}", browser);
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            
            if (!"mweb".equals(platform)) {
                webDriver.manage().window().maximize();
            }
            
            // Apply default global timeout strategy from configuration
            int defaultWait = ConfigManager.getInstance().getExplicitWait();
            webDriver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(defaultWait));
            webDriver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(defaultWait));
            webDriver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(defaultWait));

            setDriver(webDriver);
            LoggerUtil.info("Initialized {} browser driver for platform {}", browser, platform);
            
        } catch (Exception e) {
            LoggerUtil.error("Error initializing WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }
    
    public void quitDriver() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
                driver.remove();
                LoggerUtil.info("Closed browser and removed WebDriver instance");
            } catch (Exception e) {
                LoggerUtil.error("Error closing browser: {}", e.getMessage());
            }
        }
    }
}