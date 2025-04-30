package com.seleniumautomation.locators;

public class NaukriLocators {
    // Login Page
    public static final String EMAIL_FIELD = "//input[@placeholder='Enter your active Email ID / Username']";
    public static final String PASSWORD_FIELD = "//input[@placeholder='Enter your password']";
    public static final String LOGIN_BUTTON = "//button[contains(text(),'Login')]";
    public static final String OTP_FIELD = "//input[@placeholder='Enter OTP']";
    public static final String VERIFY_OTP_BUTTON = "//button[contains(text(),'Verify OTP')]";
    
    // Profile Page
    public static final String UPDATE_PROFILE_BUTTON = "//a[contains(text(),'Update Profile')]";
    public static final String UPLOAD_RESUME_BUTTON = "//input[@type='file']";
    public static final String UPLOAD_SUCCESS_MESSAGE = "//div[contains(text(),'Resume uploaded successfully')]";
    
    private NaukriLocators() {
        // Private constructor
    }
}
