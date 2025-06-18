package com.seleniumautomation.utils;

import com.restautomation.utils.LoggerUtil;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptchaReader {
    private static final String SCREENSHOT_DIR = "captcha_screenshots";
    private static final Tesseract tesseract = new Tesseract();
    
    static {
        // Initialize Tesseract with trained data
        try {
            File tessDataFolder = new File("src/main/resources/tessdata");
            if (tessDataFolder.exists()) {
                tesseract.setDatapath(tessDataFolder.getAbsolutePath());
            } else {
                // Use system installed Tesseract data
                tesseract.setDatapath("/usr/local/share/tessdata");
            }
            tesseract.setLanguage("eng");
            // Configure Tesseract for better CAPTCHA recognition
            tesseract.setPageSegMode(7); // Treat the image as a single text line
            tesseract.setTessVariable("tessedit_char_whitelist", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        } catch (Exception e) {
            LoggerUtil.error("Error initializing Tesseract: " + e.getMessage());
        }
    }
    
    public static boolean isCaptchaPresent(WebDriver driver) {
        try {
            // Check for common CAPTCHA indicators
            String pageSource = driver.getPageSource().toLowerCase();
            String title = driver.getTitle().toLowerCase();
            
            boolean isPresent = pageSource.contains("captcha") || 
                              pageSource.contains("robot check") ||
                              pageSource.contains("security check") ||
                              title.contains("robot") ||
                              title.contains("captcha");
            
            if (isPresent) {
                LoggerUtil.info("CAPTCHA detected on page");
                takeScreenshot(driver, "captcha_detected");
            }
            
            return isPresent;
        } catch (Exception e) {
            LoggerUtil.error("Error checking for CAPTCHA: " + e.getMessage());
            return false;
        }
    }
    
    public static String solveCaptcha(WebDriver driver) {
        try {
            // Find CAPTCHA image element
            WebElement captchaImage = driver.findElement(By.cssSelector("img[src*='captcha']"));
            return solveCaptcha(driver, captchaImage);
        } catch (Exception e) {
            LoggerUtil.error("Error finding CAPTCHA element: " + e.getMessage());
            return null;
        }
    }
    
    public static String solveCaptcha(WebDriver driver, WebElement captchaImage) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                LoggerUtil.info("Created screenshots directory: " + SCREENSHOT_DIR);
            }
            
            // Take screenshot of CAPTCHA
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File screenshot = new File(SCREENSHOT_DIR + "/captcha_" + timestamp + ".png");
            
            // Take element screenshot
            File elementScreenshot = captchaImage.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(elementScreenshot, screenshot);
            
            LoggerUtil.info("CAPTCHA screenshot saved: " + screenshot.getAbsolutePath());
            
            // Read CAPTCHA text using Tesseract
            String captchaText = tesseract.doOCR(screenshot).trim();
            LoggerUtil.info("Detected CAPTCHA text: " + captchaText);
            
            return captchaText;
            
        } catch (TesseractException e) {
            LoggerUtil.error("Error reading CAPTCHA: " + e.getMessage());
        } catch (IOException e) {
            LoggerUtil.error("Error saving CAPTCHA screenshot: " + e.getMessage());
        } catch (Exception e) {
            LoggerUtil.error("Unexpected error in CAPTCHA processing: " + e.getMessage());
        }
        
        return null;
    }
    
    public static void takeScreenshot(WebDriver driver, String prefix) {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(SCREENSHOT_DIR + "/" + prefix + "_" + timestamp + ".png");
            FileUtils.copyFile(screenshot, destFile);
            
            LoggerUtil.info("Screenshot saved: " + destFile.getAbsolutePath());
        } catch (Exception e) {
            LoggerUtil.error("Error taking screenshot: " + e.getMessage());
        }
    }

    public static void cleanupScreenshots() {
        try {
            File directory = new File(SCREENSHOT_DIR);
            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
                LoggerUtil.info("Cleaned screenshots directory");
            }
        } catch (IOException e) {
            LoggerUtil.error("Failed to cleanup screenshots: " + e.getMessage());
        }
    }
} 