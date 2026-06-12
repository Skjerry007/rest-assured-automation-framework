package com.seleniumautomation.test;

import com.seleniumautomation.base.BaseTest;
import com.seleniumautomation.steps.NaukriSteps;
import com.seleniumautomation.config.ConfigManager;
import com.restautomation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NaukriResumeUploadTest extends BaseTest {

    @Test(description = "Test resume upload on Naukri with OTP verification")
    public void testResumeUpload() {
        NaukriSteps naukriSteps = new NaukriSteps();
        ConfigManager config = ConfigManager.getInstance();

        try {
            naukriSteps.navigateToLogin(config.getWebUrl());
            naukriSteps.loginWithOtp(config.getNaukriEmail(), config.getNaukriPassword());
            naukriSteps.uploadResume(config.getResumePath());
            
            Assert.assertTrue(naukriSteps.verifyResumeUploadSuccess(), "Resume upload failed");
            LoggerUtil.info("Resume uploaded successfully");

        } catch (Exception e) {
            LoggerUtil.error("Error during resume upload: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
