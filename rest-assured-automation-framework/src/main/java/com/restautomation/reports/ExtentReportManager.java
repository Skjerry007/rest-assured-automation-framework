package com.restautomation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.restautomation.utils.LoggerUtil;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * ExtentReportManager - Manages ExtentReports for test reporting
 */
public class ExtentReportManager {
    private static ExtentReports extentReports;
    private static final Map<Long, ExtentTest> testMap = new HashMap<>();
    private static final String REPORT_DIR = System.getProperty("user.dir") + "/test-output/reports/";
    private static final String REPORT_NAME = "API-Test-Report-";
    
    private ExtentReportManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize the ExtentReports instance
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports initReports() {
        if (extentReports == null) {
            createReportDir();
            
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = REPORT_DIR + REPORT_NAME + timeStamp + ".html";
            
            LoggerUtil.info("Initializing Extent Reports at: {}", reportPath);
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("API Automation Test Report");
            sparkReporter.config().setReportName("REST Assured API Automation Testing");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            sparkReporter.config().setEncoding("UTF-8");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Environment", System.getProperty("env", "QA"));
        }
        return extentReports;
    }
    
    /**
     * Create test report directory if it does not exist
     */
    private static void createReportDir() {
        File directory = new File(REPORT_DIR);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                LoggerUtil.info("Created report directory: {}", REPORT_DIR);
            } else {
                LoggerUtil.error("Failed to create report directory: {}", REPORT_DIR);
            }
        }
    }
    
    /**
     * Create a test in the report
     * @param testName name of the test
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest test = initReports().createTest(testName);
        testMap.put(Thread.currentThread().getId(), test);
        return test;
    }
    
    /**
     * Create a test with category in the report
     * @param testName name of the test
     * @param categoryName category name
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createTest(String testName, String categoryName) {
        ExtentTest test = initReports().createTest(testName).assignCategory(categoryName);
        testMap.put(Thread.currentThread().getId(), test);
        return test;
    }
    
    /**
     * Get the current test for the thread
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest getTest() {
        return testMap.get(Thread.currentThread().getId());
    }
    
    /**
     * Flush the reports (write to disk)
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            LoggerUtil.info("Extent Reports flushed successfully");
        }
    }
}