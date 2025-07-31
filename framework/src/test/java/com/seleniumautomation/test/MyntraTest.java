package com.seleniumautomation.test;

import com.seleniumautomation.pages.MyntraPage;
import com.seleniumautomation.utils.LocatorUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import java.io.FileInputStream;
import java.util.Properties;

public class MyntraTest {
    private WebDriver driver;
    private MyntraPage myntraPage;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.get("https://www.myntra.com/");
        LocatorUtil.loadPropertiesFromFile("myntra-locators");
        myntraPage = new MyntraPage(driver);
    }

    @Test
    public void testAddToBagAndOpenBag() {
        myntraPage.closeCookiePopupIfPresent();
        myntraPage.searchForProduct("shirt for men");
        myntraPage.clickFirstProduct();
        String productNameOnPDP = myntraPage.getProductNameOnPDP();
        myntraPage.selectSize(); // Select first available size
        myntraPage.addToBag();
        myntraPage.goToBag(); // Click 'GO TO BAG' button
        String productNameInBag = myntraPage.getProductNameInBag();
        System.out.println("PDP Product Name: '" + productNameOnPDP + "'");
        System.out.println("Bag Product Name: '" + productNameInBag + "'");
        // Normalize for comparison
        String pdpNorm = productNameOnPDP == null ? "" : productNameOnPDP.trim().toLowerCase();
        String bagNorm = productNameInBag == null ? "" : productNameInBag.trim().toLowerCase();
        org.testng.Assert.assertTrue(bagNorm.contains(pdpNorm), "Product in bag does not match product added");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
} 