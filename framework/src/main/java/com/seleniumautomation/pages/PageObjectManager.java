package com.seleniumautomation.pages;

import org.openqa.selenium.WebDriver;

public class PageObjectManager {
    private final WebDriver driver;
    private NaukriLoginPage loginPage;
    private NaukriProfilePage profilePage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public NaukriLoginPage getLoginPage() {
        return (loginPage == null) ? loginPage = new NaukriLoginPage(driver) : loginPage;
    }

    public NaukriProfilePage getProfilePage() {
        return (profilePage == null) ? profilePage = new NaukriProfilePage(driver) : profilePage;
    }
}
