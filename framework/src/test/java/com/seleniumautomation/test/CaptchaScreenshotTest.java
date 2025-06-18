package com.seleniumautomation.test;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.utils.CaptchaReader;
import com.seleniumautomation.driver.DriverManager;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * CaptchaScreenshotTest - Tests for CAPTCHA screenshot functionality
 */
public class CaptchaScreenshotTest extends BaseTest {
    
    @Test(description = "Test CAPTCHA screenshot creation")
    public void testCaptchaScreenshot() {
        LoggerUtil.info("Starting CAPTCHA screenshot test");
        WebDriver driver = DriverManager.getInstance().getDriver();
        
        try {
            // Clean up any existing screenshots
            CaptchaReader.cleanupScreenshots();
            
            // Navigate to a page that might show CAPTCHA
            driver.get("https://www.amazon.com");
            LoggerUtil.info("Navigated to test page");

            // Check if CAPTCHA is present
            boolean captchaPresent = CaptchaReader.isCaptchaPresent(driver);
            LoggerUtil.info("CAPTCHA present: {}", captchaPresent);

            if (captchaPresent) {
                // Try to solve CAPTCHA (this will create screenshots)
                try {
                    String captchaText = CaptchaReader.solveCaptcha(driver);
                    LoggerUtil.info("CAPTCHA solved with text: {}", captchaText);
                } catch (Exception e) {
                    LoggerUtil.error("CAPTCHA solving failed: {}", e.getMessage());
                }

                // Verify screenshots were created
                File captchaScreenshot = new File("captcha_screenshots/captcha.png");
                File processedScreenshot = new File("captcha_screenshots/processed_captcha.png");
                
                // Check if files exist
                Assert.assertTrue(captchaScreenshot.exists(), "CAPTCHA screenshot should exist");
                Assert.assertTrue(processedScreenshot.exists(), "Processed CAPTCHA screenshot should exist");
                
                // Check file sizes
                Assert.assertTrue(captchaScreenshot.length() > 0, "CAPTCHA screenshot should not be empty");
                Assert.assertTrue(processedScreenshot.length() > 0, "Processed CAPTCHA screenshot should not be empty");
                
                // Log file details
                LoggerUtil.info("CAPTCHA screenshot size: {} bytes", captchaScreenshot.length());
                LoggerUtil.info("Processed CAPTCHA screenshot size: {} bytes", processedScreenshot.length());
                
                // Check if files are readable images
                try {
                    javax.imageio.ImageIO.read(captchaScreenshot);
                    javax.imageio.ImageIO.read(processedScreenshot);
                    LoggerUtil.info("Both screenshots are valid image files");
                } catch (Exception e) {
                    Assert.fail("Screenshots are not valid image files: " + e.getMessage());
                }
            } else {
                LoggerUtil.info("No CAPTCHA detected, test skipped");
            }

        } catch (Exception e) {
            LoggerUtil.error("Test failed: {}", e.getMessage());
            throw new RuntimeException("Test failed", e);
        }
    }
} 