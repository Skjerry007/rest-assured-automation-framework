package com.seleniumautomation.test;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.testng.annotations.Test;
import java.io.File;
import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.utils.WaitUtil;

/**
 * CaptchaTest - Tests for CAPTCHA handling functionality
 */
public class CaptchaTest extends BaseTest {
    @Test
    public void testReadCaptchaImage() {
        File imageFile = new File("captcha_screenshots/captcha_20250618_145555.png");
        ITesseract instance = new Tesseract();
        instance.setDatapath("tessdata"); // path to tessdata folder
        instance.setTessVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        try {
            String result = instance.doOCR(imageFile);
            System.out.println("CAPTCHA Text: " + result.trim());
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test2CaptchaNormalDemo() {
        org.openqa.selenium.WebDriver driver = com.seleniumautomation.driver.DriverManager.getInstance().getDriver();
        WaitUtil waitUtil = new WaitUtil();
        try {
            com.restautomation.utils.LoggerUtil.info("Navigating to 2Captcha normal demo page");
            driver.get("https://2captcha.com/demo/normal");
            WaitUtil.waitForPageLoad(driver);

            // Wait for the captcha image to be visible
            org.openqa.selenium.By captchaImgBy = org.openqa.selenium.By.className("_captchaImage_rrn3u_9");
            org.openqa.selenium.WebElement captchaImg = driver.findElement(captchaImgBy);
            waitUtil.waitForElementToBeVisible(captchaImg);

            // Save screenshot of captcha image
            String captchaText = com.seleniumautomation.utils.CaptchaReader.solveCaptcha(driver, captchaImg);
            com.restautomation.utils.LoggerUtil.info("Read captcha text: {}", captchaText);
            System.out.println("[2Captcha Demo] OCR Captcha Text: " + captchaText);

            // Wait for the input field and enter the captcha text
            org.openqa.selenium.By inputBy = org.openqa.selenium.By.cssSelector("input#simple-captcha-field");
            org.openqa.selenium.WebElement input = driver.findElement(inputBy);
            waitUtil.waitForElementToBeVisible(input);
            input.clear();
            input.sendKeys(captchaText);

            // Wait for the Check button and click
            org.openqa.selenium.By checkBtnBy = org.openqa.selenium.By.cssSelector("button[type='submit']");
            org.openqa.selenium.WebElement checkBtn = driver.findElement(checkBtnBy);
            waitUtil.waitForElementToBeClickable(checkBtn);
            checkBtn.click();
            WaitUtil.waitForSeconds(2);

            // Wait for the success message
            org.openqa.selenium.By successMsgBy = org.openqa.selenium.By.cssSelector("p._successMessage_rrn3u_1");
            String resultText = "";
            try {
                org.openqa.selenium.WebElement result = driver.findElement(successMsgBy);
                resultText = result.getText();
            } catch (Exception e) {
                com.restautomation.utils.LoggerUtil.error("Could not find success message: {}", e.getMessage());
            }
            com.restautomation.utils.LoggerUtil.info("2Captcha Demo Success Message: {}", resultText);
            System.out.println("[2Captcha Demo] Success Message: " + resultText);

            org.testng.Assert.assertTrue(resultText.toLowerCase().contains("captcha is passed successfully"),
                "Captcha should be solved successfully. Actual result: " + resultText);
            if (!resultText.toLowerCase().contains("captcha is passed successfully")) {
                com.restautomation.utils.LoggerUtil.error("Captcha was NOT solved successfully. Actual result: {}", resultText);
                System.err.println("[2Captcha Demo] FAILURE: Captcha was NOT solved successfully. Actual result: " + resultText);
            } else {
                com.restautomation.utils.LoggerUtil.info("Captcha was solved successfully!");
                System.out.println("[2Captcha Demo] SUCCESS: Captcha was solved successfully!");
            }
        } catch (Exception e) {
            com.restautomation.utils.LoggerUtil.error("2Captcha demo test failed: {}", e.getMessage());
            e.printStackTrace();
            org.testng.Assert.fail("2Captcha demo test failed: " + e.getMessage());
        }
    }
} 