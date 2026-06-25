package ui.tests;

import ui.base.BaseTest;
import ui.steps.NaukriSteps;
import common.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NaukriResumeUploadTest extends BaseTest {

    @Test(
        description = "Test resume upload on Naukri with OTP verification",
        dataProvider = "naukriUploadData",
        dataProviderClass = ui.providers.NaukriDataProviders.class
    )
    public void testResumeUpload(String url, String email, String password, String resumePath) throws Exception {
        NaukriSteps naukriSteps = new NaukriSteps();

        naukriSteps.navigateToLogin(url);
        naukriSteps.loginWithOtp(email, password);
        naukriSteps.uploadResume(resumePath);
        
        Assert.assertTrue(naukriSteps.verifyResumeUploadSuccess(), "Resume upload failed");
        LoggerUtil.info("Resume uploaded successfully");
    }
}
