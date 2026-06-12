package com.automation.ui.pages;

import com.automation.ui.base.BasePage;
import com.automation.ui.locators.NaukriLocators;
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
        clickElement(updateProfileButton);
    }

    public void uploadResume(String filePath) {
        uploadFile(uploadResumeButton, filePath);
    }

    public boolean isUploadSuccessful() {
        return isElementDisplayed(uploadSuccessMessage);
    }
}
