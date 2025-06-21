package com.seleniumautomation.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.restautomation.utils.LoggerUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CDPNetworkInterceptTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        LoggerUtil.info("Setting up ChromeDriver for network interception test");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        LoggerUtil.info("Injecting JavaScript for network request interception");
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.interceptedRequests = [];" +
            "const originalFetch = window.fetch;" +
            "window.fetch = function() {" +
            "    window.interceptedRequests.push(arguments[0]);" +
            "    console.log('[INTERCEPT] Fetch request: ' + arguments[0]);" +
            "    return originalFetch.apply(this, arguments);" +
            "};" +
            "const originalXHR = window.XMLHttpRequest.prototype.open;" +
            "window.XMLHttpRequest.prototype.open = function() {" +
            "    window.interceptedRequests.push(arguments[1]);" +
            "    console.log('[INTERCEPT] XHR request: ' + arguments[1]);" +
            "    originalXHR.apply(this, arguments);" +
            "};"
        );
        LoggerUtil.info("JavaScript injection completed successfully");
    }

    @Test
    public void testInterceptHttpbinRequest() {
        LoggerUtil.info("Starting network interception test on httpbin.org/get");
        driver.get("https://httpbin.org/get");

        List<String> requests = null;
        // Wait up to 10 seconds for the request to be captured
        for (int i = 0; i < 10; i++) {
            requests = (List<String>) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return window.interceptedRequests;");
            if (requests != null && !requests.isEmpty()) {
                LoggerUtil.info("Found " + requests.size() + " intercepted requests after " + (i+1) + " seconds");
                break;
            }
            try { 
                Thread.sleep(1000); 
                LoggerUtil.debug("Waiting for requests to be intercepted... (attempt " + (i+1) + "/10)");
            } catch (InterruptedException ignored) {}
        }

        if (requests == null || requests.isEmpty()) {
            LoggerUtil.warn("No requests were intercepted after waiting 10 seconds");
            return;
        }

        LoggerUtil.info("Processing " + requests.size() + " intercepted requests");
        for (String url : requests) {
            LoggerUtil.info("Intercepted URL: " + url);
            if (url.contains("httpbin.org/get")) {
                LoggerUtil.info("Successfully intercepted the httpbin.org/get request");
                Map<String, String> params = getQueryParams(url);
                if (!params.isEmpty()) {
                    LoggerUtil.info("Query Parameters found:");
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        LoggerUtil.info("  " + entry.getKey() + " = " + entry.getValue());
                    }
                } else {
                    LoggerUtil.info("No query parameters found in this request");
                }
            }
        }
    }

    @Test
    public void testInterceptAllAmazonRequests() {
        LoggerUtil.info("Starting JavaScript-based network interception test on amazon.in");
        ChromeDriver chromeDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            chromeDriver = new ChromeDriver(options);

            // Inject JavaScript to intercept all network requests
            LoggerUtil.info("Injecting JavaScript for comprehensive network request interception");
            chromeDriver.executeScript(
                "window.allInterceptedRequests = [];" +
                "window.requestCount = 0;" +
                "const originalFetch = window.fetch;" +
                "window.fetch = function() {" +
                "    const url = arguments[0];" +
                "    window.allInterceptedRequests.push(url);" +
                "    window.requestCount++;" +
                "    console.log('[JS-INTERCEPT] Fetch request #' + window.requestCount + ': ' + url);" +
                "    return originalFetch.apply(this, arguments);" +
                "};" +
                "const originalXHR = window.XMLHttpRequest.prototype.open;" +
                "window.XMLHttpRequest.prototype.open = function() {" +
                "    const url = arguments[1];" +
                "    window.allInterceptedRequests.push(url);" +
                "    window.requestCount++;" +
                "    console.log('[JS-INTERCEPT] XHR request #' + window.requestCount + ': ' + url);" +
                "    originalXHR.apply(this, arguments);" +
                "};"
            );
            LoggerUtil.info("JavaScript injection completed successfully");

            LoggerUtil.info("Navigating to https://www.amazon.in/");
            chromeDriver.get("https://www.amazon.in/");

            // Wait for page to load and capture requests
            LoggerUtil.info("Waiting for page to load and capture network requests...");
            try { Thread.sleep(20000); } catch (InterruptedException ignored) {}

            // Get all intercepted requests
            List<String> allRequests = (List<String>) chromeDriver.executeScript("return window.allInterceptedRequests;");
            Integer requestCount = (Integer) chromeDriver.executeScript("return window.requestCount;");

            if (allRequests != null && !allRequests.isEmpty()) {
                LoggerUtil.info("Successfully intercepted " + requestCount + " network requests");
                LoggerUtil.info("=== ALL INTERCEPTED REQUESTS ===");
                
                for (int i = 0; i < allRequests.size(); i++) {
                    String url = allRequests.get(i);
                    LoggerUtil.info("Request #" + (i + 1) + ": " + url);
                    
                    // Check for specific Amazon requests
                    if (url.contains("fetchshoppingaids")) {
                        LoggerUtil.info("*** FOUND fetchshoppingaids request! ***");
                        Map<String, String> params = getQueryParams(url);
                        if (!params.isEmpty()) {
                            LoggerUtil.info("Query Parameters for fetchshoppingaids:");
                            for (Map.Entry<String, String> entry : params.entrySet()) {
                                LoggerUtil.info("  " + entry.getKey() + " = " + entry.getValue());
                            }
                        }
                    }
                }
                LoggerUtil.info("=== END OF REQUESTS ===");
            } else {
                LoggerUtil.warn("No network requests were intercepted");
            }

        } finally {
            if (chromeDriver != null) {
                LoggerUtil.info("Closing ChromeDriver (Amazon interception test)");
                chromeDriver.quit();
            }
        }
    }

    @Test
    public void testInterceptNykaaRefreshRequest() {
        LoggerUtil.info("Starting JavaScript-based network interception test on nykaa.com/shoppingBag (Mobile Web)");
        ChromeDriver chromeDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            // Mobile web user agent to bypass bot detection
            options.addArguments("user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_7_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Mobile/15E148 Safari/604.1");
            // Set mobile viewport
            options.addArguments("--window-size=375,667");
            chromeDriver = new ChromeDriver(options);

            // Set mobile viewport
            chromeDriver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));

            // Inject JavaScript to intercept all network requests
            LoggerUtil.info("Injecting JavaScript for comprehensive network request interception");
            chromeDriver.executeScript(
                "window.interceptedRequests = [];" +
                "const originalFetch = window.fetch;" +
                "window.fetch = function() {" +
                "    window.interceptedRequests.push(arguments[0]);" +
                "    return originalFetch.apply(this, arguments);" +
                "};" +
                "const originalXHR = window.XMLHttpRequest.prototype.open;" +
                "window.XMLHttpRequest.prototype.open = function() {" +
                "    window.interceptedRequests.push(arguments[1]);" +
                "    return originalXHR.apply(this, arguments);" +
                "};"
            );
            LoggerUtil.info("JavaScript injection completed successfully");

            // Navigate to Nykaa shopping bag
            LoggerUtil.info("Navigating to https://www.nykaa.com/shoppingBag");
            chromeDriver.get("https://www.nykaa.com/shoppingBag");

            // Wait for page to load and capture requests
            LoggerUtil.info("Waiting for page to load and capture network requests for 20 seconds...");
            try { Thread.sleep(20000); } catch (InterruptedException ignored) {}

            // Get intercepted requests
            List<String> requests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");

            if (requests == null || requests.isEmpty()) {
                LoggerUtil.warn("No network requests were intercepted on Nykaa");
            } else {
                LoggerUtil.info("Intercepted " + requests.size() + " network requests:");
                for (String url : requests) {
                    LoggerUtil.info("Request: " + url);
                    if (url.contains("refresh")) {
                        LoggerUtil.info("*** FOUND REFRESH REQUEST: " + url + " ***");
                    }
                }
            }

        } catch (Exception e) {
            LoggerUtil.error("Error during Nykaa network interception test: " + e.getMessage());
        } finally {
            if (chromeDriver != null) {
                LoggerUtil.info("Closing ChromeDriver (Nykaa interception test)");
                chromeDriver.quit();
            }
        }
    }

    @Test
    public void testInterceptTheInternetRequests() {
        LoggerUtil.info("Starting network interception test on the-internet.herokuapp.com");
        ChromeDriver chromeDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            chromeDriver = new ChromeDriver(options);

            // Inject JavaScript to intercept all network requests
            LoggerUtil.info("Injecting JavaScript for comprehensive network request interception");
            chromeDriver.executeScript(
                "window.interceptedRequests = [];" +
                "const originalFetch = window.fetch;" +
                "window.fetch = function() {" +
                "    window.interceptedRequests.push(arguments[0]);" +
                "    console.log('Intercepted fetch request:', arguments[0]);" +
                "    return originalFetch.apply(this, arguments);" +
                "};" +
                "const originalXHR = window.XMLHttpRequest.prototype.open;" +
                "window.XMLHttpRequest.prototype.open = function() {" +
                "    window.interceptedRequests.push(arguments[1]);" +
                "    console.log('Intercepted XHR request:', arguments[1]);" +
                "    return originalXHR.apply(this, arguments);" +
                "};" +
                "console.log('Network interception script injected successfully');"
            );
            LoggerUtil.info("JavaScript injection completed successfully");

            // Navigate to the-internet.herokuapp.com
            LoggerUtil.info("Navigating to https://the-internet.herokuapp.com");
            chromeDriver.get("https://the-internet.herokuapp.com");

            // Wait for page to load and capture requests
            LoggerUtil.info("Waiting for page to load and capture network requests for 5 seconds...");
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}

            // Get intercepted requests
            List<String> requests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");

            if (requests == null || requests.isEmpty()) {
                LoggerUtil.warn("No network requests were intercepted on the-internet.herokuapp.com");
            } else {
                LoggerUtil.info("Intercepted " + requests.size() + " network requests:");
                for (String url : requests) {
                    LoggerUtil.info("Request: " + url);
                }
            }

            // Navigate to a page that might have more dynamic content
            LoggerUtil.info("Navigating to Dynamic Controls page to trigger more requests...");
            chromeDriver.get("https://the-internet.herokuapp.com/dynamic_controls");
            
            // Wait for page to load
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            
            // Try to click a button that might trigger AJAX
            try {
                LoggerUtil.info("Attempting to click 'Remove' button to trigger AJAX request...");
                chromeDriver.findElement(By.cssSelector("button[onclick*='swapCheckbox']")).click();
                LoggerUtil.info("Button clicked successfully");
                
                // Wait for AJAX response
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            } catch (Exception e) {
                LoggerUtil.warn("Could not find or click the button: " + e.getMessage());
            }
            
            // Get updated requests
            List<String> updatedRequests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");
            
            if (updatedRequests != null && !updatedRequests.isEmpty()) {
                LoggerUtil.info("Total intercepted requests after interactions: " + updatedRequests.size());
                for (String url : updatedRequests) {
                    LoggerUtil.info("Request: " + url);
                }
            } else {
                LoggerUtil.warn("Still no network requests intercepted after interactions");
            }

        } catch (Exception e) {
            LoggerUtil.error("Error during the-internet network interception test: " + e.getMessage());
        } finally {
            if (chromeDriver != null) {
                LoggerUtil.info("Closing ChromeDriver (the-internet interception test)");
                chromeDriver.quit();
            }
        }
    }

    @Test
    public void testInterceptTestPagesFormRequests() {
        LoggerUtil.info("Starting network interception test on testpages.herokuapp.com basic HTML form");
        ChromeDriver chromeDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            chromeDriver = new ChromeDriver(options);

            // Inject JavaScript to intercept all network requests
            LoggerUtil.info("Injecting JavaScript for network request interception");
            chromeDriver.executeScript(
                "window.interceptedRequests = [];" +
                "const originalFetch = window.fetch;" +
                "window.fetch = function() {" +
                "    window.interceptedRequests.push(arguments[0]);" +
                "    console.log('Intercepted fetch request:', arguments[0]);" +
                "    return originalFetch.apply(this, arguments);" +
                "};" +
                "const originalXHR = window.XMLHttpRequest.prototype.open;" +
                "window.XMLHttpRequest.prototype.open = function() {" +
                "    window.interceptedRequests.push(arguments[1]);" +
                "    console.log('Intercepted XHR request:', arguments[1]);" +
                "    return originalXHR.apply(this, arguments);" +
                "};"
            );
            LoggerUtil.info("JavaScript injection completed successfully");

            // Navigate to the test page
            LoggerUtil.info("Navigating to https://testpages.herokuapp.com/styled/basic-html-form-test.html");
            chromeDriver.get("https://testpages.herokuapp.com/styled/basic-html-form-test.html");

            // Wait for page to load
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

            // Fill out the form
            LoggerUtil.info("Filling out the form");
            chromeDriver.findElement(By.name("username")).sendKeys("testuser");
            chromeDriver.findElement(By.name("password")).sendKeys("testpass");
            chromeDriver.findElement(By.name("comments")).sendKeys("This is a test comment");

            // Submit the form
            LoggerUtil.info("Submitting the form");
            chromeDriver.findElement(By.cssSelector("input[type='submit']")).click();

            // Wait for the request to be sent and page to load
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

            // Get intercepted requests
            List<String> requests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");

            if (requests == null || requests.isEmpty()) {
                LoggerUtil.warn("No network requests were intercepted on testpages.herokuapp.com");
            } else {
                LoggerUtil.info("Intercepted " + requests.size() + " network requests:");
                for (String url : requests) {
                    LoggerUtil.info("Request: " + url);
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during testpages form network interception test: " + e.getMessage());
        } finally {
            if (chromeDriver != null) {
                LoggerUtil.info("Closing ChromeDriver (testpages form interception test)");
                chromeDriver.quit();
            }
        }
    }

    @Test
    public void testInterceptW3SchoolsAjaxRequests() {
        LoggerUtil.info("Starting network interception test on W3Schools AJAX demo page");
        ChromeDriver chromeDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            // Run in visible mode to see the AJAX request happening
            // options.addArguments("--headless");
            chromeDriver = new ChromeDriver(options);

            // Inject JavaScript to intercept all network requests
            LoggerUtil.info("Injecting JavaScript for comprehensive network request interception");
            chromeDriver.executeScript(
                "window.interceptedRequests = [];" +
                "const originalFetch = window.fetch;" +
                "window.fetch = function() {" +
                "    window.interceptedRequests.push(arguments[0]);" +
                "    console.log('Intercepted fetch request:', arguments[0]);" +
                "    return originalFetch.apply(this, arguments);" +
                "};" +
                "const originalXHR = window.XMLHttpRequest.prototype.open;" +
                "window.XMLHttpRequest.prototype.open = function() {" +
                "    window.interceptedRequests.push(arguments[1]);" +
                "    console.log('Intercepted XHR request:', arguments[1]);" +
                "    return originalXHR.apply(this, arguments);" +
                "};" +
                "console.log('Network interception script injected successfully');"
            );
            LoggerUtil.info("JavaScript injection completed successfully");

            // Navigate to W3Schools AJAX demo page
            LoggerUtil.info("Navigating to W3Schools AJAX demo page");
            chromeDriver.get("https://www.w3schools.com/xml/tryit.asp?filename=tryajax_first");

            // Wait for page to load
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

            // Switch to the iframe that contains the demo
            LoggerUtil.info("Switching to demo iframe");
            WebElement iframe = chromeDriver.findElement(By.id("iframeResult"));
            chromeDriver.switchTo().frame(iframe);

            // Wait a bit more for iframe content to load
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

            // Get initial requests (should be empty)
            List<String> initialRequests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");
            LoggerUtil.info("Initial intercepted requests: " + (initialRequests != null ? initialRequests.size() : 0));

            // Click the button that triggers the AJAX request
            LoggerUtil.info("Clicking 'Change Content' button to trigger AJAX request");
            WebElement changeButton = chromeDriver.findElement(By.cssSelector("button[onclick*='loadDoc']"));
            changeButton.click();

            // Wait for the AJAX request to complete
            LoggerUtil.info("Waiting for AJAX request to complete...");
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

            // Get intercepted requests after clicking the button
            List<String> requests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");

            if (requests == null || requests.isEmpty()) {
                LoggerUtil.warn("No network requests were intercepted on W3Schools AJAX demo");
                LoggerUtil.info("This might be because the request is made in a different context or the interception script didn't work in the iframe");
            } else {
                LoggerUtil.info("SUCCESS! Intercepted " + requests.size() + " network requests:");
                for (String url : requests) {
                    LoggerUtil.info("Request: " + url);
                }
            }

            // Also check if the content changed (to verify the AJAX worked)
            try {
                WebElement contentDiv = chromeDriver.findElement(By.id("demo"));
                String content = contentDiv.getText();
                LoggerUtil.info("Content after AJAX request: " + content);
            } catch (Exception e) {
                LoggerUtil.warn("Could not verify content change: " + e.getMessage());
            }

        } catch (Exception e) {
            LoggerUtil.error("Error during W3Schools AJAX network interception test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (chromeDriver != null) {
                LoggerUtil.info("Closing ChromeDriver (W3Schools AJAX interception test)");
                chromeDriver.quit();
            }
        }
    }

    private Map<String, String> getQueryParams(String url) {
        Map<String, String> params = new HashMap<>();
        try {
            String[] parts = url.split("\\?");
            if (parts.length > 1) {
                String query = parts[1];
                LoggerUtil.debug("Parsing query string: " + query);
                for (String param : query.split("&")) {
                    String[] pair = param.split("=", 2);
                    String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                    String value = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
                    params.put(key, value);
                    LoggerUtil.debug("Parsed parameter: " + key + " = " + value);
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Error parsing query params: " + e.getMessage(), e);
        }
        return params;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            LoggerUtil.info("Closing ChromeDriver");
            driver.quit();
        }
    }
} 