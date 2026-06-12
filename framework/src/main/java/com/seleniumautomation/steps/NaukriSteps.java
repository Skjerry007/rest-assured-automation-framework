package com.seleniumautomation.steps;

import com.seleniumautomation.pages.NaukriLoginPage;
import com.seleniumautomation.pages.NaukriProfilePage;
import com.seleniumautomation.pages.PageObjectManager;
import com.seleniumautomation.driver.DriverManager;
import com.seleniumautomation.utils.GmailService;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.WebDriver;

public class NaukriSteps {

    private final PageObjectManager pageObjectManager;
    private final NaukriLoginPage loginPage;
    private final NaukriProfilePage profilePage;

    public NaukriSteps() {
        this(DriverManager.getInstance().getDriver());
    }

    public NaukriSteps(WebDriver driver) {
        this.pageObjectManager = new PageObjectManager(driver);
        this.loginPage = this.pageObjectManager.getLoginPage();
        this.profilePage = this.pageObjectManager.getProfilePage();
    }

    public NaukriSteps(PageObjectManager pageObjectManager) {
        this.pageObjectManager = pageObjectManager;
        this.loginPage = this.pageObjectManager.getLoginPage();
        this.profilePage = this.pageObjectManager.getProfilePage();
    }

    public void navigateToLogin(String url) {
        loginPage.navigateToUrl(url);
    }

    public void loginWithOtp(String email, String password) throws Exception {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        
        String otp = GmailService.getOTPFromEmail("Naukri Login OTP");
        loginPage.enterOtp(otp);
        loginPage.clickVerifyOtp();
    }

    public void uploadResume(String filePath) {
        profilePage.clickUpdateProfile();
        profilePage.uploadResume(filePath);
    }

    public boolean verifyResumeUploadSuccess() {
        return profilePage.isUploadSuccessful();
    }
}
