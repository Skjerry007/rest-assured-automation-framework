package com.seleniumautomation.test;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.testng.annotations.Test;
import java.io.File;

/**
 * CaptchaTest - Tests for CAPTCHA handling functionality
 */
public class CaptchaTest {
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
} 