package com.seleniumautomation.utils;

import com.restautomation.utils.LoggerUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CourseContentScraper {
    
    public static void saveCourseContentToProperties(String courseTitle, Map<String, List<String>> courseContent, String outputPath) {
        Properties properties = new Properties();
        
        try {
            LoggerUtil.info("Saving course content to properties file: {}", outputPath);
            
            // Add course title
            properties.setProperty("course.title", courseTitle);
            
            // Add course content
            int sectionIndex = 1;
            for (Map.Entry<String, List<String>> entry : courseContent.entrySet()) {
                String sectionTitle = entry.getKey();
                List<String> lectures = entry.getValue();
                
                // Add section title
                properties.setProperty("section." + sectionIndex + ".title", sectionTitle);
                
                // Add lectures for this section
                for (int i = 0; i < lectures.size(); i++) {
                    String lectureTitle = lectures.get(i);
                    properties.setProperty("section." + sectionIndex + ".lecture." + (i + 1), lectureTitle);
                }
                
                // Add lecture count for this section
                properties.setProperty("section." + sectionIndex + ".lectureCount", String.valueOf(lectures.size()));
                
                sectionIndex++;
            }
            
            // Add total section count
            properties.setProperty("total.sections", String.valueOf(courseContent.size()));
            
            // Save to file
            try (OutputStream output = new FileOutputStream(outputPath)) {
                properties.store(output, "Course Content - " + courseTitle);
                LoggerUtil.info("Course content saved successfully to: {}", outputPath);
            }
            
        } catch (IOException e) {
            LoggerUtil.error("Failed to save course content to properties file: {}", e.getMessage());
            throw new RuntimeException("Failed to save course content", e);
        }
    }
    
    public static void printCourseContent(String courseTitle, Map<String, List<String>> courseContent) {
        LoggerUtil.info("=== COURSE CONTENT EXTRACTION RESULTS ===");
        LoggerUtil.info("Course Title: {}", courseTitle);
        LoggerUtil.info("Total Sections: {}", courseContent.size());
        LoggerUtil.info("");
        
        int sectionNumber = 1;
        for (Map.Entry<String, List<String>> entry : courseContent.entrySet()) {
            String sectionTitle = entry.getKey();
            List<String> lectures = entry.getValue();
            
            LoggerUtil.info("Section {}: {}", sectionNumber, sectionTitle);
            LoggerUtil.info("Lectures ({})", lectures.size());
            
            for (int i = 0; i < lectures.size(); i++) {
                LoggerUtil.info("  {}. {}", i + 1, lectures.get(i));
            }
            
            LoggerUtil.info("");
            sectionNumber++;
        }
        
        LoggerUtil.info("=== END COURSE CONTENT ===");
    }
} 