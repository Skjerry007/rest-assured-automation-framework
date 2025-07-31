package com.seleniumautomation.utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.HashMap;
import java.util.Map;

public class WebDriverSetup {
    public static WebDriver setupChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        // Disable automation flags
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        // Set a realistic user-agent
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        // Optionally, use a real Chrome user profile (uncomment and set path if needed)
        // options.addArguments("user-data-dir=/path/to/your/custom/profile");
        return new ChromeDriver(options);
    }
} 