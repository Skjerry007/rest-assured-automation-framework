# REST Assured & Selenium + Self-Healing Automation Framework

This framework provides robust, maintainable, and scalable automation for both REST APIs and web UI (Selenium) with advanced features like self-healing locators, Dockerized Selenium Grid, and beautiful reporting.

## Features

### UI Automation (SauceDemo Example)
- **Full end-to-end automation** of https://www.saucedemo.com (login, add to cart, checkout, order verification, negative scenarios)
- **Self-healing locators**: Automatically recover from UI changes using multiple locator strategies (see `LocatorUtil.selfHealing`)
- **Explicit waits**: All dynamic elements are handled with robust waits
- **Parallel & Grid execution**: Run tests locally or on Selenium Grid (Docker Compose)
- **Retry mechanism**: Flaky tests are retried automatically
- **Comprehensive logging**: All steps and errors are logged
- **Screenshots on failure**: For easy debugging
- **Beautiful ExtentReports**: Interactive HTML reports for both UI and API tests

### API Automation
- REST Assured for API testing (GET, POST, PUT, DELETE, schema validation, etc.)
- Mock API support (WireMock)
- Secure credential management (AWS/Google Secret Manager)
- Data-driven and environment-driven

### Reporting
- **ExtentReports** for both UI and API (HTML, screenshots, logs, system info)
- **Log4j2** for detailed logs
- **TestNG listeners** for reporting and retry

### Self-Healing Locators
- Implemented in `framework/src/main/java/com/seleniumautomation/utils/LocatorUtil.java`
- Use `LocatorUtil.selfHealing(By... locators)` in page objects
- Tries each locator in order until one matches, making tests resilient to UI changes

### Chrome DevTools Protocol (CDP) Network Interception
- **JavaScript-based Network Request Interception**: Intercept and monitor all network requests (fetch, XMLHttpRequest) using injected JavaScript
- **Comprehensive Request Monitoring**: Capture URLs, query parameters, and request details for debugging and validation
- **Multi-Site Testing**: Tested on various websites including:
  - **httpbin.org**: Basic HTTP request interception and parameter extraction
  - **Amazon.in**: E-commerce site request monitoring (including `fetchshoppingaids` requests)
  - **Nykaa.com**: Mobile web request interception with custom user agents
  - **the-internet.herokuapp.com**: Dynamic content and AJAX request monitoring
  - **testpages.herokuapp.com**: Form submission request capture
  - **W3Schools AJAX Demo**: Real-time AJAX request interception in iframes

#### CDP Features Implemented:
- **Request URL Capture**: Intercept and log all network request URLs
- **Query Parameter Parsing**: Extract and validate URL parameters
- **Request Counting**: Track total number of intercepted requests
- **Console Logging**: Real-time request logging in browser console
- **Mobile Web Support**: Custom user agents and viewport settings for mobile testing
- **Iframe Support**: Network interception works within iframe contexts
- **Form Submission Monitoring**: Capture form submission requests and parameters

#### Usage Example:
```java
// Inject network interception JavaScript
chromeDriver.executeScript(
    "window.interceptedRequests = [];" +
    "const originalFetch = window.fetch;" +
    "window.fetch = function() {" +
    "    window.interceptedRequests.push(arguments[0]);" +
    "    return originalFetch.apply(this, arguments);" +
    "};"
);

// Navigate to page and wait for requests
chromeDriver.get("https://example.com");
Thread.sleep(5000);

// Get intercepted requests
List<String> requests = (List<String>) chromeDriver.executeScript("return window.interceptedRequests;");
```

#### Test Classes:
- `CDPNetworkInterceptTest.java`: Comprehensive network interception tests
- Supports multiple interception strategies and websites
- Includes parameter extraction and validation utilities

### Docker & Selenium Grid
- **docker-compose.yml** for easy Selenium Grid setup (hub + Chrome + Firefox nodes)
- Run UI tests in parallel across browsers/containers

## Prerequisites

- Java 21+
- Maven
- Google Chrome (for local UI tests)
- Docker Desktop (for Selenium Grid)
- Tesseract OCR (for CAPTCHA tests, optional)

## Installation & Setup

1. **Clone the repository**
2. **Install dependencies:**
   ```bash
   mvn clean install
   ```
3. **Install Tesseract OCR** (optional, for CAPTCHA):
   ```bash
   brew install tesseract   # macOS
   sudo apt-get install tesseract-ocr   # Ubuntu/Debian
   ```
4. **Set up config:**
   ```bash
   cp framework/src/test/resources/config/dev-config.example.properties framework/src/test/resources/config/dev-config.properties
   # Edit dev-config.properties for your environment/credentials
   ```
5. **(Optional) Set up Secret Manager** for secure credentials (see README section above)

## Running UI Tests

### **Locally (single or parallel):**
```bash
# Run all UI tests (TestNG suite)
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng-selenium.xml

# Run a specific test class
mvn test -Dtest=SauceDemoTest

# Run in parallel (locally)
mvn clean test -DexecutorCapacity=2 -Dsurefire.suiteXmlFiles=src/test/resources/testng-selenium.xml
```

### **CDP Network Interception Tests:**
```bash
# Run all CDP network interception tests
mvn test -Dtest=CDPNetworkInterceptTest

# Run specific CDP test methods
mvn test -Dtest=CDPNetworkInterceptTest#testInterceptHttpbinRequest
mvn test -Dtest=CDPNetworkInterceptTest#testInterceptAllAmazonRequests
mvn test -Dtest=CDPNetworkInterceptTest#testInterceptNykaaRefreshRequest
```

**Note**: CDP tests require Chrome browser and may need to run in non-headless mode for some scenarios (e.g., W3Schools AJAX demo).

### **On Selenium Grid (Docker Compose):**
1. **Start the grid:**
   ```bash
   docker-compose up -d
   # Grid will be at http://localhost:4444
   ```
2. **Run tests on grid:**
   ```bash
   mvn clean test -DgridExecutorCapacity=2 -Dsurefire.suiteXmlFiles=src/test/resources/testng-selenium.xml
   ```
   - Increase `gridExecutorCapacity` for more parallelism

## Running API Tests
```bash
# Run all API tests
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng-restassured.xml

# Run a specific API test class
mvn test -Dtest=UserApiTest
```

## Reporting & Logs
- **ExtentReports HTML:**
  - UI: `framework/test-output/extent-reports/ExtentReport_*.html`
  - API: `framework/test-output/reports/API-Test-Report-*.html`
- **Logs:**
  - `framework/logs/test-automation.log`
- **Screenshots (on failure):**
  - `framework/captcha_screenshots/` (for CAPTCHA)
  - Embedded in ExtentReports for UI failures

## Docker Compose for Selenium Grid
Example `docker-compose.yml`:
```yaml
version: '3.7'
services:
  selenium-hub:
    image: selenium/hub:4.21.0
    container_name: selenium-hub
    ports:
      - "4444:4444"
    environment:
      - GRID_MAX_SESSION=16
      - GRID_BROWSER_TIMEOUT=300
      - GRID_TIMEOUT=300
  chrome:
    image: selenium/node-chrome:4.21.0
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
    volumes:
      - /dev/shm:/dev/shm
  firefox:
    image: selenium/node-firefox:4.21.0
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
    volumes:
      - /dev/shm:/dev/shm
```

## SauceDemo UI Automation Details
- **Tested Flows:**
  - Login (positive/negative)
  - Add to cart, cart validation
  - Checkout (positive/negative, missing info)
  - Order summary, price, tax, payment, shipping
  - Order completion and thank you page
- **Self-healing locators** ensure resilience to UI changes
- **All data is extracted and asserted** (item name, price, totals, etc.)
- **Parallel/grid execution** for speed and scalability
- **Full logs and screenshots** for debugging

## Best Practices
- Use self-healing locators for all new page objects
- Keep credentials and secrets out of source code (use secret manager/config)
- Use Docker Compose for scalable, reproducible grid runs
- Review ExtentReports after every run for actionable insights
- Add new tests using the Page Object Model for maintainability

## Troubleshooting
- If browsers do not launch, check Docker and Selenium Grid status
- If locators break, add fallback strategies to `LocatorUtil.selfHealing`
- For slow tests, tune explicit wait times in config
- For more parallelism, increase node count in `docker-compose.yml` and `gridExecutorCapacity`

## Contributors & License
- See `CONTRIBUTING.md` and `LICENSE` 