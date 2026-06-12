package ui.locators;

import org.openqa.selenium.By;
import common.config.ConfigManager;

public class NaukriLocators {

    private static boolean isMobile() {
        return "mweb".equalsIgnoreCase(ConfigManager.getInstance().getPlatform());
    }

    public static class Login {
        public static By emailField() {
            return isMobile() 
                ? By.xpath("//input[@id='email']") 
                : By.xpath("//input[@id='email']");
        }

        public static By passwordField() {
            return isMobile() 
                ? By.xpath("//input[@id='password']") 
                : By.xpath("//input[@id='password']");
        }

        public static By loginButton() {
            return By.xpath("//button[@id='login']");
        }

        public static By otpField() {
            return By.xpath("//input[@id='otp']");
        }

        public static By verifyOtpButton() {
            return By.xpath("//button[@id='verifyOtp']");
        }
    }

    public static class Profile {
        public static By updateProfileButton() {
            return By.xpath("//button[@id='updateProfile']");
        }

        public static By uploadResumeButton() {
            return By.xpath("//input[@id='uploadResume']");
        }

        public static By uploadSuccessMessage() {
            return By.xpath("//div[@class='success-message']");
        }
    }
}
