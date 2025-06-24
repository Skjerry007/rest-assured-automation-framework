package com.seleniumautomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.seleniumautomation.utils.WaitUtil;
import com.restautomation.utils.LoggerUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.interactions.Actions;
import java.util.Random;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UdemyCoursePage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators
    private By courseTitleLocator = By.cssSelector("h1[data-purpose='course-title']");
    private By courseContentSectionLocator = By.cssSelector("[data-purpose='curriculum-section']");
    private By sectionTitleLocator = By.cssSelector("[data-purpose='section-title']");
    private By lectureItemsLocator = By.cssSelector("[data-purpose='lecture-item']");
    private By lectureTitleLocator = By.cssSelector("[data-purpose='lecture-title']");
    private By popupCloseButtonLocator = By.cssSelector("[data-purpose='close-modal']");
    private By popupOverlayLocator = By.cssSelector("[data-purpose='modal-overlay']");
    
    public UdemyCoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    public void navigateToCourse(String courseUrl) {
        LoggerUtil.info("Navigating to course URL: {}", courseUrl);
        
        // Add random delay to simulate human behavior
        randomDelay(1000, 3000);
        
        driver.get(courseUrl);
        
        // Execute stealth scripts to bypass detection
        executeStealthScripts();
        
        // Add cookies to appear more human-like
        addHumanLikeCookies();
        
        WaitUtil.waitForPageLoad(driver);
        
        // Random delay after page load
        randomDelay(2000, 4000);
    }
    
    private void executeStealthScripts() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Remove webdriver property
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined});");
            
            // Override permissions
            js.executeScript("Object.defineProperty(navigator, 'permissions', {get: () => ({query: () => Promise.resolve({state: 'granted'})})});");
            
            // Override plugins
            js.executeScript("Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]});");
            
            // Override languages
            js.executeScript("Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});");
            
            // Override chrome
            js.executeScript("Object.defineProperty(window, 'chrome', {get: () => ({runtime: {}})});");
            
            LoggerUtil.info("Stealth scripts executed successfully");
        } catch (Exception e) {
            LoggerUtil.warn("Some stealth scripts failed: {}", e.getMessage());
        }
    }
    
    private void addHumanLikeCookies() {
        try {
            // Add common cookies that real browsers have
            driver.manage().addCookie(new Cookie("__cf_bm", "random_value", ".udemy.com", "/", null));
            driver.manage().addCookie(new Cookie("_ga", "GA1.1.random_value", ".udemy.com", "/", null));
            driver.manage().addCookie(new Cookie("_gid", "GA1.1.random_value", ".udemy.com", "/", null));
            LoggerUtil.info("Human-like cookies added");
        } catch (Exception e) {
            LoggerUtil.warn("Failed to add cookies: {}", e.getMessage());
        }
    }
    
    private void randomDelay(int minMs, int maxMs) {
        try {
            Random random = new Random();
            int delay = random.nextInt(maxMs - minMs + 1) + minMs;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void humanLikeScroll() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Actions actions = new Actions(driver);
            
            // Scroll down gradually like a human
            for (int i = 0; i < 5; i++) {
                js.executeScript("window.scrollBy(0, " + (200 + new Random().nextInt(300)) + ");");
                randomDelay(500, 1500);
            }
            
            // Scroll back up a bit
            js.executeScript("window.scrollBy(0, -" + (100 + new Random().nextInt(200)) + ");");
            randomDelay(500, 1000);
            
            LoggerUtil.info("Human-like scrolling completed");
        } catch (Exception e) {
            LoggerUtil.warn("Human-like scrolling failed: {}", e.getMessage());
        }
    }

    public void handlePopup() {
        try {
            LoggerUtil.info("Attempting to handle popup if present");
            randomDelay(1000, 2000);
            
            // Wait for popup to appear
            if (wait.until(ExpectedConditions.elementToBeClickable(popupCloseButtonLocator)) != null) {
                WebElement closeButton = driver.findElement(popupCloseButtonLocator);
                closeButton.click();
                LoggerUtil.info("Popup closed successfully");
                randomDelay(1000, 2000);
            }
        } catch (Exception e) {
            LoggerUtil.info("No popup found or popup handling failed: {}", e.getMessage());
            
            // Alternative: click on overlay to close popup
            try {
                WebElement overlay = driver.findElement(popupOverlayLocator);
                overlay.click();
                LoggerUtil.info("Clicked on overlay to close popup");
                randomDelay(1000, 2000);
            } catch (Exception ex) {
                LoggerUtil.info("No overlay found, continuing without popup handling");
            }
        }
    }
    
    public String getCourseTitle() {
        // Try old selector first
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(courseTitleLocator));
            String title = titleElement.getText().trim();
            LoggerUtil.info("Course title extracted: {}", title);
            return title;
        } catch (Exception e) {
            LoggerUtil.warn("Old selector for course title failed, trying dynamic approach: {}", e.getMessage());
        }
        // Dynamic: find first h1 or h2 with text containing 'Selenium' (course name)
        try {
            List<WebElement> headings = driver.findElements(By.xpath("//h1|//h2"));
            for (WebElement heading : headings) {
                String text = heading.getText().trim();
                if (!text.isEmpty() && text.toLowerCase().contains("selenium")) {
                    LoggerUtil.info("Dynamic course title found: {}", text);
                    return text;
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Dynamic course title extraction failed: {}", e.getMessage());
        }
        return "Unknown Course Title";
    }
    
    public Map<String, List<String>> extractCourseContent() {
        Map<String, List<String>> courseContent = new HashMap<>();
        
        LoggerUtil.info("Starting course content extraction with anti-detection measures");
        
        // Perform human-like scrolling first
        humanLikeScroll();
        
        // Wait for content to load
        randomDelay(3000, 5000);
        
        // Strategy 1: Try old selectors first
        courseContent = tryOldSelectors();
        if (!courseContent.isEmpty()) {
            LoggerUtil.info("Successfully extracted content using old selectors");
            return courseContent;
        }
        
        // Strategy 2: Try JavaScript-based extraction
        courseContent = extractWithJavaScript();
        if (!courseContent.isEmpty()) {
            LoggerUtil.info("Successfully extracted content using JavaScript");
            return courseContent;
        }
        
        // Strategy 3: Try DOM traversal with multiple patterns
        courseContent = extractWithDOMPatterns();
        if (!courseContent.isEmpty()) {
            LoggerUtil.info("Successfully extracted content using DOM patterns");
            return courseContent;
        }
        
        // Strategy 4: Try scroll-based detection
        courseContent = extractWithScrolling();
        if (!courseContent.isEmpty()) {
            LoggerUtil.info("Successfully extracted content using scroll-based detection");
            return courseContent;
        }
        
        LoggerUtil.warn("All extraction methods failed, returning empty content");
        return courseContent;
    }
    
    private Map<String, List<String>> tryOldSelectors() {
        Map<String, List<String>> courseContent = new HashMap<>();
        
        try {
            LoggerUtil.info("Trying old selector for curriculum section");
            wait.until(ExpectedConditions.presenceOfElementLocated(courseContentSectionLocator));
            List<WebElement> sections = driver.findElements(courseContentSectionLocator);
            for (WebElement section : sections) {
                try {
                    WebElement sectionTitleElement = section.findElement(sectionTitleLocator);
                    String sectionTitle = sectionTitleElement.getText().trim();
                    List<String> lectures = new ArrayList<>();
                    List<WebElement> lectureElements = section.findElements(lectureItemsLocator);
                    for (WebElement lecture : lectureElements) {
                        try {
                            WebElement lectureTitleElement = lecture.findElement(lectureTitleLocator);
                            String lectureTitle = lectureTitleElement.getText().trim();
                            if (!lectureTitle.isEmpty()) lectures.add(lectureTitle);
                        } catch (Exception ignore) {}
                    }
                    if (!sectionTitle.isEmpty()) courseContent.put(sectionTitle, lectures);
                } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            LoggerUtil.warn("Old selector for curriculum failed: {}", e.getMessage());
        }
        
        return courseContent;
    }
    
    private Map<String, List<String>> extractWithJavaScript() {
        Map<String, List<String>> courseContent = new HashMap<>();
        
        try {
            LoggerUtil.info("Trying JavaScript-based extraction");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // More targeted JavaScript to find actual course curriculum
            String jsScript = """
                function extractCourseContent() {
                    let sections = {};
                    
                    // Strategy 1: Look specifically for course curriculum container
                    let curriculumContainer = null;
                    
                    // Try to find the main curriculum section
                    let possibleContainers = [
                        document.querySelector('[data-purpose="curriculum-section"]'),
                        document.querySelector('[data-testid="curriculum-section"]'),
                        document.querySelector('.curriculum-section'),
                        document.querySelector('.course-content'),
                        document.querySelector('[class*="curriculum"]'),
                        document.querySelector('[class*="content"]')
                    ];
                    
                    for (let container of possibleContainers) {
                        if (container && container.offsetParent) {
                            let text = container.textContent.trim();
                            if (text.length > 500 && !text.toLowerCase().includes('contact us') && 
                                !text.toLowerCase().includes('about us') && !text.toLowerCase().includes('privacy policy')) {
                                curriculumContainer = container;
                                break;
                            }
                        }
                    }
                    
                    // Strategy 2: Look for elements containing "Section" and "Lecture" patterns
                    if (!curriculumContainer) {
                        let allElements = document.querySelectorAll('*');
                        for (let elem of allElements) {
                            if (elem.offsetParent && elem.children.length > 5) {
                                let text = elem.textContent.trim();
                                if (text.includes('Section') && text.includes('Lecture') && 
                                    text.length > 1000 && !text.toLowerCase().includes('contact us')) {
                                    curriculumContainer = elem;
                                    break;
                                }
                            }
                        }
                    }
                    
                    // Strategy 3: Look for the largest container with section-like content
                    if (!curriculumContainer) {
                        let allDivs = document.querySelectorAll('div');
                        let maxLength = 0;
                        for (let div of allDivs) {
                            if (div.offsetParent) {
                                let text = div.textContent.trim();
                                if (text.length > maxLength && text.includes('Section') && 
                                    !text.toLowerCase().includes('contact us') && 
                                    !text.toLowerCase().includes('about us')) {
                                    maxLength = text.length;
                                    curriculumContainer = div;
                                }
                            }
                        }
                    }
                    
                    if (curriculumContainer) {
                        // Extract sections from the curriculum container
                        let sectionElements = curriculumContainer.querySelectorAll('[class*="section"], [class*="chapter"], [class*="module"]');
                        
                        if (sectionElements.length === 0) {
                            // Try to find sections by text content
                            let allDivs = curriculumContainer.querySelectorAll('div');
                            for (let div of allDivs) {
                                if (div.textContent && div.textContent.includes('Section') && 
                                    div.children.length > 2) {
                                    sectionElements = [div];
                                    break;
                                }
                            }
                        }
                        
                        let sectionIndex = 1;
                        for (let section of sectionElements) {
                            if (!section.offsetParent) continue;
                            
                            let sectionTitle = 'Section ' + sectionIndex++;
                            
                            // Try to get section title
                            let titleElement = section.querySelector('h3, h4, h5, strong, b, [class*="title"]');
                            if (titleElement && titleElement.textContent.trim().length > 3) {
                                let title = titleElement.textContent.trim();
                                if (title.length < 100 && !title.toLowerCase().includes('share') && 
                                    !title.toLowerCase().includes('gift') && !title.toLowerCase().includes('apply')) {
                                    sectionTitle = title;
                                }
                            }
                            
                            let lectures = [];
                            
                            // Extract lectures - be more specific about what constitutes a lecture
                            let lectureElements = section.querySelectorAll('[class*="lecture"], [class*="lesson"], li');
                            
                            for (let lecture of lectureElements) {
                                if (!lecture.offsetParent) continue;
                                
                                let lectureText = lecture.textContent.trim();
                                if (lectureText.length > 5 && lectureText.length < 200) {
                                    // Validate lecture text - exclude UI elements
                                    let lowerLectureText = lectureText.toLowerCase();
                                    if (!lowerLectureText.includes('contact us') && 
                                        !lowerLectureText.includes('about us') && 
                                        !lowerLectureText.includes('privacy policy') &&
                                        !lowerLectureText.includes('terms') &&
                                        !lowerLectureText.includes('help') &&
                                        !lowerLectureText.includes('support') &&
                                        !lowerLectureText.includes('blog') &&
                                        !lowerLectureText.includes('affiliate') &&
                                        !lowerLectureText.includes('investors') &&
                                        !lowerLectureText.includes('careers') &&
                                        !lowerLectureText.includes('english') &&
                                        !lowerLectureText.includes('udemy business') &&
                                        !lowerLectureText.includes('teach on udemy') &&
                                        !lowerLectureText.includes('get the app') &&
                                        !lowerLectureText.includes('share') &&
                                        !lowerLectureText.includes('gift') &&
                                        !lowerLectureText.includes('apply') &&
                                        !lowerLectureText.includes('buy') &&
                                        !lowerLectureText.includes('coupon') &&
                                        !lowerLectureText.includes('unavailable')) {
                                        lectures.push(lectureText);
                                    }
                                }
                            }
                            
                            // Only add section if it has meaningful lectures
                            if (lectures.length > 0 && lectures.length > 1) {
                                sections[sectionTitle] = lectures;
                            }
                        }
                    }
                    
                    return sections;
                }
                return extractCourseContent();
            """;
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) js.executeScript(jsScript);
            
            if (result != null && !result.isEmpty()) {
                for (Map.Entry<String, Object> entry : result.entrySet()) {
                    String sectionTitle = entry.getKey();
                    @SuppressWarnings("unchecked")
                    List<Object> lectureObjects = (List<Object>) entry.getValue();
                    List<String> lectures = new ArrayList<>();
                    
                    for (Object obj : lectureObjects) {
                        if (obj instanceof String) {
                            lectures.add((String) obj);
                        }
                    }
                    
                    if (!lectures.isEmpty()) {
                        courseContent.put(sectionTitle, lectures);
                    }
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.warn("JavaScript extraction failed: {}", e.getMessage());
        }
        
        return courseContent;
    }
    
    private Map<String, List<String>> extractWithDOMPatterns() {
        Map<String, List<String>> courseContent = new HashMap<>();
        
        try {
            LoggerUtil.info("Trying DOM pattern-based extraction");
            
            // Multiple selectors to try
            String[] selectors = {
                "[data-purpose*='curriculum']",
                "[data-purpose*='section']", 
                "[data-purpose*='lecture']",
                "[class*='curriculum']",
                "[class*='content']",
                "[role='region']",
                "[role='tabpanel']",
                "section",
                "div[class*='section']",
                "div[class*='chapter']",
                "div[class*='module']"
            };
            
            for (String selector : selectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    for (WebElement element : elements) {
                        if (!element.isDisplayed()) continue;
                        
                        String text = element.getText().trim();
                        if (text.length() < 200) continue;
                        
                        // Validate content
                        if (isValidCurriculumContent(text)) {
                            courseContent = extractSectionsFromElement(element);
                            if (!courseContent.isEmpty()) {
                                LoggerUtil.info("Found curriculum content using selector: {}", selector);
                                return courseContent;
                            }
                        }
                    }
                } catch (Exception ignore) {}
            }
            
        } catch (Exception e) {
            LoggerUtil.warn("DOM pattern extraction failed: {}", e.getMessage());
        }
        
        return courseContent;
    }
    
    private boolean isValidCurriculumContent(String text) {
        String lowerText = text.toLowerCase();
        
        // Must contain curriculum keywords
        boolean hasCurriculumKeywords = lowerText.contains("section") || 
                                      lowerText.contains("lecture") || 
                                      lowerText.contains("chapter") ||
                                      lowerText.contains("module") ||
                                      lowerText.contains("lesson");
        
        // Must NOT contain footer/navigation keywords that indicate wrong content
        boolean hasFooterKeywords = lowerText.contains("contact us") || 
                                  lowerText.contains("about us") || 
                                  lowerText.contains("careers") ||
                                  lowerText.contains("privacy policy") ||
                                  lowerText.contains("terms") ||
                                  lowerText.contains("help") ||
                                  lowerText.contains("support") ||
                                  lowerText.contains("blog") ||
                                  lowerText.contains("affiliate") ||
                                  lowerText.contains("investors") ||
                                  lowerText.contains("sitemap") ||
                                  lowerText.contains("accessibility") ||
                                  lowerText.contains("english") ||
                                  lowerText.contains("udemy business") ||
                                  lowerText.contains("teach on udemy") ||
                                  lowerText.contains("get the app");
        
        // Must NOT contain UI/promotional keywords
        boolean hasUIKeywords = lowerText.contains("share") ||
                              lowerText.contains("gift") ||
                              lowerText.contains("apply") ||
                              lowerText.contains("buy") ||
                              lowerText.contains("coupon") ||
                              lowerText.contains("unavailable") ||
                              lowerText.contains("discount") ||
                              lowerText.contains("price") ||
                              lowerText.contains("enroll") ||
                              lowerText.contains("purchase");
        
        return hasCurriculumKeywords && !hasFooterKeywords && !hasUIKeywords;
    }
    
    private Map<String, List<String>> extractSectionsFromElement(WebElement element) {
        Map<String, List<String>> sections = new HashMap<>();
        
        try {
            // Try multiple approaches to find sections
            List<WebElement> sectionElements = new ArrayList<>();
            
            // Approach 1: Look for section-like elements
            sectionElements = element.findElements(By.xpath(".//div[contains(@class, 'section') or contains(@class, 'chapter') or contains(@class, 'module')]"));
            
            // Approach 2: Look for collapsible elements
            if (sectionElements.isEmpty()) {
                sectionElements = element.findElements(By.xpath(".//div[@role='button'] | .//button"));
            }
            
            // Approach 3: Look for elements with section-like text
            if (sectionElements.isEmpty()) {
                sectionElements = element.findElements(By.xpath(".//*[contains(text(), 'Section') or contains(text(), 'Chapter') or contains(text(), 'Module')]"));
            }
            
            // Approach 4: Look for list items
            if (sectionElements.isEmpty()) {
                sectionElements = element.findElements(By.xpath(".//li"));
            }
            
            int sectionIndex = 1;
            for (WebElement section : sectionElements) {
                if (!section.isDisplayed()) continue;
                
                String sectionTitle = "Section " + sectionIndex++;
                List<String> lectures = new ArrayList<>();
                
                // Try to get section title
                try {
                    WebElement titleElement = section.findElement(By.xpath(".//h3 | .//h4 | .//strong | .//b | .//span[contains(@class, 'title')]"));
                    if (titleElement != null && !titleElement.getText().trim().isEmpty()) {
                        String title = titleElement.getText().trim();
                        if (title.length() > 3 && title.length() < 100) {
                            sectionTitle = title;
                        }
                    }
                } catch (Exception ignore) {}
                
                // Extract lectures
                lectures = extractLecturesFromSection(section);
                
                if (!lectures.isEmpty() && lectures.size() > 1) {
                    sections.put(sectionTitle, lectures);
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.warn("Section extraction failed: {}", e.getMessage());
        }
        
        return sections;
    }
    
    private List<String> extractLecturesFromSection(WebElement section) {
        List<String> lectures = new ArrayList<>();
        
        try {
            // Try multiple approaches to find lectures
            List<WebElement> lectureElements = new ArrayList<>();
            
            // Approach 1: Look for lecture-like elements
            lectureElements = section.findElements(By.xpath(".//div[contains(@class, 'lecture') or contains(@class, 'lesson')]"));
            
            // Approach 2: Look for list items
            if (lectureElements.isEmpty()) {
                lectureElements = section.findElements(By.xpath(".//li"));
            }
            
            // Approach 3: Look for clickable elements
            if (lectureElements.isEmpty()) {
                lectureElements = section.findElements(By.xpath(".//a | .//button"));
            }
            
            for (WebElement lecture : lectureElements) {
                if (!lecture.isDisplayed()) continue;
                
                String lectureText = lecture.getText().trim();
                if (isValidLectureText(lectureText)) {
                    lectures.add(lectureText);
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.warn("Lecture extraction failed: {}", e.getMessage());
        }
        
        return lectures;
    }
    
    private boolean isValidLectureText(String text) {
        if (text.isEmpty() || text.length() < 3) return false;
        if (text.length() > 200) return false;
        
        String lowerText = text.toLowerCase();
        
        // Check for invalid lecture text patterns
        String[] invalidPatterns = {
            "contact us", "about us", "careers", "privacy policy", "terms", 
            "help", "support", "blog", "affiliate", "investors", "sitemap",
            "accessibility", "english", "udemy business", "teach on udemy",
            "get the app", "cookie", "advertising", "mobile apps",
            "share", "gift", "apply", "buy", "coupon", "unavailable",
            "discount", "price", "enroll", "purchase", "subscribe",
            "free", "limited time", "offer", "deal", "sale"
        };
        
        for (String pattern : invalidPatterns) {
            if (lowerText.contains(pattern)) return false;
        }
        
        // Must contain some educational content indicators
        boolean hasEducationalContent = lowerText.contains("introduction") ||
                                      lowerText.contains("setup") ||
                                      lowerText.contains("installation") ||
                                      lowerText.contains("configuration") ||
                                      lowerText.contains("basics") ||
                                      lowerText.contains("fundamentals") ||
                                      lowerText.contains("advanced") ||
                                      lowerText.contains("example") ||
                                      lowerText.contains("demo") ||
                                      lowerText.contains("practice") ||
                                      lowerText.contains("exercise") ||
                                      lowerText.contains("project") ||
                                      lowerText.contains("test") ||
                                      lowerText.contains("quiz") ||
                                      lowerText.contains("assignment") ||
                                      lowerText.contains("overview") ||
                                      lowerText.contains("summary") ||
                                      lowerText.contains("conclusion") ||
                                      lowerText.contains("selenium") ||
                                      lowerText.contains("webdriver") ||
                                      lowerText.contains("java") ||
                                      lowerText.contains("automation") ||
                                      lowerText.contains("framework") ||
                                      lowerText.contains("testing") ||
                                      lowerText.contains("element") ||
                                      lowerText.contains("locator") ||
                                      lowerText.contains("assertion") ||
                                      lowerText.contains("wait") ||
                                      lowerText.contains("driver") ||
                                      lowerText.contains("browser") ||
                                      lowerText.contains("page") ||
                                      lowerText.contains("object") ||
                                      lowerText.contains("model") ||
                                      lowerText.contains("pattern") ||
                                      lowerText.contains("strategy") ||
                                      lowerText.contains("method") ||
                                      lowerText.contains("class") ||
                                      lowerText.contains("interface") ||
                                      lowerText.contains("exception") ||
                                      lowerText.contains("error") ||
                                      lowerText.contains("debug") ||
                                      lowerText.contains("log") ||
                                      lowerText.contains("report") ||
                                      lowerText.contains("jenkins") ||
                                      lowerText.contains("maven") ||
                                      lowerText.contains("git") ||
                                      lowerText.contains("version") ||
                                      lowerText.contains("control");
        
        return hasEducationalContent;
    }
    
    private Map<String, List<String>> extractWithScrolling() {
        Map<String, List<String>> courseContent = new HashMap<>();
        
        try {
            LoggerUtil.info("Trying scroll-based curriculum detection");
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            int scrollHeight = 0;
            int maxScrolls = 15;
            
            for (int i = 0; i < maxScrolls; i++) {
                scrollHeight += 400;
                js.executeScript("window.scrollTo(0, " + scrollHeight + ");");
                randomDelay(1000, 2000);
                
                // Check if we found curriculum content
                List<WebElement> potentialContainers = driver.findElements(By.xpath("//*[contains(text(), 'Section') or contains(text(), 'Lecture') or contains(text(), 'Chapter')]"));
                
                for (WebElement element : potentialContainers) {
                    if (element.isDisplayed()) {
                        try {
                            WebElement container = element.findElement(By.xpath("./ancestor::div[1]"));
                            if (isValidCurriculumContent(container.getText())) {
                                courseContent = extractSectionsFromElement(container);
                                if (!courseContent.isEmpty()) {
                                    LoggerUtil.info("Found curriculum content while scrolling");
                                    return courseContent;
                                }
                            }
                        } catch (Exception ignore) {}
                    }
                }
            }
            
        } catch (Exception e) {
            LoggerUtil.warn("Scroll-based curriculum detection failed: {}", e.getMessage());
        }
        
        return courseContent;
    }
    
    public void scrollToCourseContent() {
        try {
            LoggerUtil.info("Scrolling to course content section");
            WebElement contentSection = driver.findElement(courseContentSectionLocator);
            WaitUtil.scrollToElement(driver, contentSection);
            WaitUtil.waitForSeconds(2);
        } catch (Exception e) {
            LoggerUtil.warn("Failed to scroll to course content: {}", e.getMessage());
        }
    }
} 