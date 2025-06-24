package com.seleniumautomation.test;

import com.seleniumautomation.pages.UdemyCoursePage;
import com.seleniumautomation.utils.CourseContentScraper;
import com.seleniumautomation.driver.DriverManager;
import com.restautomation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class UdemyCourseScrapingTest {
    private static final String UDEMY_COURSE_URL = "https://www.udemy.com/course/selenium-real-time-examplesinterview-questions/?couponCode=ST16MT230625G1";
    private static final String OUTPUT_PROPERTIES_FILE = "udemy-course-content.properties";

    @Test(description = "Scrape Udemy course content and save to properties file")
    public void testUdemyCourseContentScraping() {
        UdemyCoursePage udemyPage = null;
        try {
            LoggerUtil.info("Starting Udemy course content scraping test");
            DriverManager.getInstance().initializeDriver();
            udemyPage = new UdemyCoursePage(DriverManager.getInstance().getDriver());
            udemyPage.navigateToCourse(UDEMY_COURSE_URL);
            udemyPage.handlePopup();
            Thread.sleep(3000);
            String courseTitle = udemyPage.getCourseTitle();
            Assert.assertNotNull(courseTitle, "Course title should not be null");
            Assert.assertFalse(courseTitle.isEmpty(), "Course title should not be empty");
            LoggerUtil.info("Course title: {}", courseTitle);
            udemyPage.scrollToCourseContent();
            Thread.sleep(2000);
            Map<String, List<String>> courseContent = udemyPage.extractCourseContent();
            Assert.assertNotNull(courseContent, "Course content should not be null");
            Assert.assertFalse(courseContent.isEmpty(), "Course content should not be empty");
            LoggerUtil.info("Extracted {} sections from course content", courseContent.size());
            CourseContentScraper.printCourseContent(courseTitle, courseContent);
            String outputPath = "src/test/resources/" + OUTPUT_PROPERTIES_FILE;
            CourseContentScraper.saveCourseContentToProperties(courseTitle, courseContent, outputPath);
            LoggerUtil.info("Udemy course content scraping completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Test failed with exception: {}", e.getMessage());
            Assert.fail("Test failed: " + e.getMessage(), e);
        } finally {
            if (DriverManager.getInstance().getDriver() != null) {
                DriverManager.getInstance().quitDriver();
            }
        }
    }
} 