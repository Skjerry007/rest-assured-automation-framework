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

public class CaptchaReader {
    private static final Tesseract tesseract = new Tesseract();
    
    static {
        // Set the path to the tessdata directory
        tesseract.setDatapath("src/main/resources/tessdata");
        // Set language to eng
        tesseract.setLanguage("eng");
    }

    public static String readCaptchaText(WebDriver driver) throws IOException, TesseractException {
        // Take screenshot of the CAPTCHA
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        Path screenshotPath = Paths.get("target", "captcha.png");
        Files.copy(screenshot.toPath(), screenshotPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Read the CAPTCHA text using Tesseract
        String captchaText = tesseract.doOCR(screenshot);
        
        // Clean up the text (remove spaces and special characters)
        captchaText = captchaText.replaceAll("[^a-zA-Z0-9]", "").trim();
        
        return captchaText;
    }

    public static void solveCaptcha(WebDriver driver) throws IOException, TesseractException {
        try {
            // Find the CAPTCHA input field
            WebElement captchaInput = driver.findElement(By.id("captchacharacters"));
            
            // Read the CAPTCHA text
            String captchaText = readCaptchaText(driver);
            
            // Type the CAPTCHA text
            captchaInput.sendKeys(captchaText);
            
            // Find and click the submit button
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 