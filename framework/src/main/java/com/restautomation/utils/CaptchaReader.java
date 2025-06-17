package com.restautomation.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptchaReader {
    private static final Tesseract tesseract = new Tesseract();
    private static final Logger logger = LoggerFactory.getLogger(CaptchaReader.class);
    
    static {
        // Set the path to the tessdata directory
        tesseract.setDatapath("src/main/resources/tessdata");
        // Set language to eng
        tesseract.setLanguage("eng");
    }

    public static String readCaptchaText(WebDriver driver) throws IOException, TesseractException {
        logger.info("Attempting to locate CAPTCHA image...");
        WebElement captchaImage = null;
        // Try multiple selectors for the CAPTCHA image
        By[] imageLocators = new By[] {
            By.id("auth-captcha-image"),
            By.cssSelector("img.a-captcha-image"),
            By.xpath("//img[contains(@src, 'captcha')]"),
            By.xpath("//img[contains(@alt, 'captcha')]"),
        };
        for (By locator : imageLocators) {
            try {
                captchaImage = driver.findElement(locator);
                if (captchaImage.isDisplayed()) {
                    logger.info("Found CAPTCHA image using locator: {}", locator.toString());
                    break;
                }
            } catch (Exception ignored) {}
        }
        if (captchaImage == null) {
            logger.error("CAPTCHA image not found with any known locator.");
            throw new RuntimeException("CAPTCHA image not found");
        }
        // Take screenshot of the CAPTCHA image only
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);
        org.openqa.selenium.Point point = captchaImage.getLocation();
        int eleWidth = captchaImage.getSize().getWidth();
        int eleHeight = captchaImage.getSize().getHeight();
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        File captchaFile = new File("target/captcha.png");
        ImageIO.write(eleScreenshot, "png", captchaFile);
        logger.info("Saved CAPTCHA image to: {}", captchaFile.getAbsolutePath());
        // OCR
        String result = tesseract.doOCR(captchaFile);
        logger.info("OCR result: {}", result);
        result = result.replaceAll("[^a-zA-Z0-9]", "");
        if (result.isEmpty()) {
            logger.error("OCR failed to read CAPTCHA. Check saved image at: {}", captchaFile.getAbsolutePath());
        }
        return result;
    }

    public static void solveCaptcha(WebDriver driver) throws IOException, TesseractException {
        logger.info("Attempting to solve CAPTCHA...");
        String captchaText = readCaptchaText(driver);
        logger.info("Read CAPTCHA text: {}", captchaText);
        // Try multiple selectors for the input field
        WebElement captchaInput = null;
        By[] inputLocators = new By[] {
            By.id("captchacharacters"),
            By.cssSelector("input[name='field-keywords']"),
            By.xpath("//input[contains(@name, 'captcha')]"),
            By.xpath("//input[contains(@id, 'captcha')]"),
        };
        for (By locator : inputLocators) {
            try {
                captchaInput = driver.findElement(locator);
                if (captchaInput.isDisplayed()) {
                    logger.info("Found CAPTCHA input using locator: {}", locator.toString());
                    break;
                }
            } catch (Exception ignored) {}
        }
        if (captchaInput == null) {
            logger.error("CAPTCHA input field not found with any known locator.");
            throw new RuntimeException("CAPTCHA input field not found");
        }
        captchaInput.clear();
        captchaInput.sendKeys(captchaText);
        // Try to find and click the submit button
        By[] submitLocators = new By[] {
            By.cssSelector("button[type='submit']"),
            By.id("a-autoid-0"),
            By.xpath("//button[contains(text(),'Submit') or contains(text(),'Continue')]"),
            By.xpath("//input[@type='submit']"),
        };
        boolean submitted = false;
        for (By locator : submitLocators) {
            try {
                WebElement submitBtn = driver.findElement(locator);
                if (submitBtn.isDisplayed() && submitBtn.isEnabled()) {
                    submitBtn.click();
                    logger.info("Clicked CAPTCHA submit button using locator: {}", locator.toString());
                    submitted = true;
                    break;
                }
            } catch (Exception ignored) {}
        }
        if (!submitted) {
            logger.error("CAPTCHA submit button not found or not clickable.");
            throw new RuntimeException("CAPTCHA submit button not found");
        }
        // Wait for the page to load after CAPTCHA submission
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Try to click 'Continue Shopping' button if present
        By[] continueShoppingLocators = new By[] {
            By.xpath("//a[contains(text(),'Continue Shopping')]"),
            By.xpath("//button[contains(text(),'Continue Shopping')]"),
            By.xpath("//input[@value='Continue Shopping']"),
        };
        for (By locator : continueShoppingLocators) {
            try {
                WebElement continueBtn = driver.findElement(locator);
                if (continueBtn.isDisplayed() && continueBtn.isEnabled()) {
                    continueBtn.click();
                    logger.info("Clicked 'Continue Shopping' button using locator: {}", locator.toString());
                    break;
                }
            } catch (Exception ignored) {}
        }
    }
} 