package com.automation.ui.pages;

import com.automation.ui.base.BasePage;
import com.automation.ui.locators.NaukriLocators;
import com.automation.common.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NaukriProfilePage extends BasePage {
    private final By updateProfileButton = NaukriLocators.Profile.updateProfileButton();
    private final By uploadResumeButton = NaukriLocators.Profile.uploadResumeButton();
    private final By uploadSuccessMessage = NaukriLocators.Profile.uploadSuccessMessage();

    public NaukriProfilePage(WebDriver driver) {
        super(driver);
    }

    public void clickUpdateProfile() {
        try {
            LoggerUtil.info("Clicking update profile button");
            clickElement(updateProfileButton);
            LoggerUtil.info("Successfully clicked update profile button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to click update profile button: {}", e.getMessage());
            throw new RuntimeException("Failed to click update profile button", e);
        }
    }

    public void uploadResume(String filePath) {
        try {
            LoggerUtil.info("Uploading resume from file path: {}", filePath);
            uploadFile(uploadResumeButton, filePath);
            LoggerUtil.info("Successfully uploaded resume");
        } catch (Exception e) {
            LoggerUtil.error("Failed to upload resume: {}", e.getMessage());
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    public boolean isUploadSuccessful() {
        try {
            LoggerUtil.info("Checking if resume upload was successful");
            boolean result = isElementDisplayed(uploadSuccessMessage);
            LoggerUtil.info("Resume upload success status: {}", result);
            return result;
        } catch (Exception e) {
            LoggerUtil.warn("Error checking resume upload success status: {}", e.getMessage());
            return false;
        }
    }
}
