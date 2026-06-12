package com.automation.ui.tests;

import com.automation.ui.base.BaseTest;
import com.automation.ui.steps.NaukriSteps;
import com.automation.common.config.ConfigManager;
import com.automation.common.utils.LoggerUtil;
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
