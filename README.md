# REST Assured & Selenium + Self-Healing Automation Framework

This framework provides robust, maintainable, and scalable automation for both REST APIs and web UI (Selenium) with advanced features like self-healing locators, Dockerized Selenium Grid, and beautiful reporting.

## Features

### UI Automation (Naukri Example)
- **Full end-to-end automation** of Naukri resume upload workflow with OTP retrieval
- **Global Default Timeout Strategy**: Configured implicit waits, page load timeouts, and script timeouts globally at the driver initialization level
- **Thread-safe WebDriver management**: Robust parallel execution support
- **Retry mechanism**: TestNG retry analyzer dynamically applied to retry failed tests automatically
- **Comprehensive logging**: Structured Logging utilizing Log4j2
- **Screenshots on failure**: Automatically captured and attached to reports
- **Beautiful Allure Reports**: Modern reporting with step-by-step documentation, API payload attachments, and screenshots on failure

### API Automation
- **REST Assured** for API testing (GET, POST, PUT, DELETE, schema validation, etc.)
- **Response Validation Helpers**: Simplified assertion and deserialization methods
- **Mock API support** via WireMock
- **Secure credential management** (AWS/Google Secret Manager)
- **Data-driven & environment-driven** execution

### Reporting
- **Allure Reports** integration with AspectJ runtime weaving for dynamic step capture and failures attachments
- **Log4j2** for detailed logs
- **TestNG listeners** for execution tracing and runtime retry transformer

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

### Project Structure

```
rest-assured-automation-framework/
├── framework/                           # Main project directory
│   ├── pom.xml                         # ✅ Single, complete Maven POM
│   ├── src/                            # Source code
│   │   ├── main/java/                  # Java source files
│   │   │   ├── common/                 # Common / Shared components
│   │   │   │   ├── config/             # Config manager loading config.properties
│   │   │   │   │   └── ConfigManager.java
│   │   │   │   ├── listeners/          # TestNG execution listeners and Retry Analyzers
│   │   │   │   │   ├── AnnotationTransformer.java
│   │   │   │   │   ├── RetryAnalyzer.java
│   │   │   │   │   └── TestListener.java
│   │   │   │   ├── secretmanager/      # AWS/Google secrets management
│   │   │   │   │   ├── GmailSecretManager.java
│   │   │   │   │   └── SecretManager.java
│   │   │   │   └── utils/              # Shared utility helpers
│   │   │   │       ├── JWTUtil.java
│   │   │   │       ├── LoggerUtil.java
│   │   │   │       ├── TestDataUtil.java
│   │   │   │       ├── GmailService.java
│   │   │   │       └── WaitUtil.java
│   │   │   ├── api/                    # REST API automation
│   │   │   │   ├── base/               # Base specs & request wrappers
│   │   │   │   │   └── BaseAPI.java
│   │   │   │   ├── constants/          # API constants
│   │   │   │   │   ├── Endpoints.java
│   │   │   │   │   └── StatusCodes.java
│   │   │   │   ├── endpoints/          # API endpoint client classes
│   │   │   │   │   ├── AlbumAPI.java
│   │   │   │   │   ├── AuthAPI.java
│   │   │   │   │   ├── PhotoAPI.java
│   │   │   │   │   ├── PostAPI.java
│   │   │   │   │   ├── TodoAPI.java
│   │   │   │   │   └── UserAPI.java
│   │   │   │   ├── exceptions/         # Custom exceptions
│   │   │   │   │   └── APIException.java
│   │   │   │   ├── models/             # Data model POJOs
│   │   │   │   │   ├── Album.java
│   │   │   │   │   ├── AuthRequest.java
│   │   │   │   │   ├── AuthResponse.java
│   │   │   │   │   ├── Photo.java
│   │   │   │   │   ├── Post.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   ├── Todo.java
│   │   │   │   │   └── User.java
│   │   │   │   └── utils/              # API specific utils
│   │   │   │       └── ResponseValidator.java
│   │   │   └── ui/                     # Selenium UI automation
│   │   │       ├── base/               # Base page
│   │   │       │   └── BasePage.java
│   │   │       ├── driver/             # Thread-local WebDriver management
│   │   │       │   └── DriverManager.java
│   │   │       ├── locators/           # UI elements locators mapping
│   │   │       │   └── NaukriLocators.java
│   │   │       ├── pages/              # Page Object Model classes
│   │   │       │   ├── PageObjectManager.java
│   │   │       │   ├── NaukriLoginPage.java
│   │   │       │   └── NaukriProfilePage.java
│   │   │       └── steps/              # E2E step scenarios
│   │   │           └── NaukriSteps.java
│   │   ├── test/java/                  # Test files
│   │   │   ├── api/tests/              # API tests
│   │   │   │   ├── AlbumApiTest.java
│   │   │   │   ├── APIExceptionHandlingTest.java
│   │   │   │   ├── PhotoApiTest.java
│   │   │   │   ├── PostApiTest.java
│   │   │   │   ├── TodoApiTest.java
│   │   │   │   ├── UserApiTest.java
│   │   │   │   └── WireMockAPITest.java
│   │   │   └── ui/tests/               # UI tests
│   │   │       ├── BaseTest.java
│   │   │       └── NaukriResumeUploadTest.java
│   └── test/resources/                 # Test resources
│       ├── config/                     # Test configuration
│       │   ├── dev-config.example.properties
│       │   ├── dev-config.properties
│       │   └── qa-config.properties
│       ├── log4j2.xml                  # Logging configuration
│       ├── rest-assured.properties     # REST Assured config
│       ├── schemas/                    # JSON schemas
│       │   └── user-schema.json
│       ├── testdata/                   # Test data files
│       │   ├── posts.json
│       │   └── users.json
│       └── testng-*.xml               # TestNG suite files
│           ├── testng-restassured.xml
│           ├── testng-selenium.xml
│           └── testng.xml
├── docker-compose.yml                  # ✅ Selenium Grid setup
├── README.md                           # This file
├── .gitignore                          # Git ignore rules
├── SECURITY.md                         # Security guidelines
├── setup-secrets.sh                    # Secret setup script
└── local-secrets.properties            # Local secrets (gitignored)
```

### Key Features of the Structure:

✅ **Single Maven POM**: Simplified structure with one complete `pom.xml` in the framework directory

✅ **Centralized Locators**: All UI locators stored in properties files under `src/main/resources/com/seleniumautomation/locators/`

✅ **Self-Healing Locators**: Advanced locator management in `LocatorUtil.java` with automatic fallback strategies

✅ **Page Object Model**: Clean separation of page objects, utilities, and test classes

✅ **Modular Design**: Separate packages for REST API and Selenium automation

✅ **Comprehensive Testing**: Both API and UI tests with proper test organization

✅ **Docker Support**: Ready-to-use Selenium Grid with Docker Compose

✅ **Configuration Management**: Environment-specific config files and secret management

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
- **Allure Reports:**
  - Allure Results directory: `framework/target/allure-results`
  - Generate and serve report:
    ```bash
    allure serve target/allure-results
    ```
- **Logs:**
  - Log output directory: `framework/logs/test-automation.log`
- **Screenshots (on failure):**
  - Dynamically captured and attached directly inside Allure Report cases on TestNG failure.

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

## Naukri UI Automation Details
- **Tested Flows:**
  - Login page credentials input (positive/negative scenarios)
  - OTP retrieval via Gmail API (`GmailService`)
  - Resume upload flow page navigation
- **Global timeout strategy** manages waits implicitly at the driver creation stage
- **All credentials** are fetched dynamically from GCP/AWS Secret Manager or system properties
- **Full Log4j2 execution tracing** recorded per test method

## Best Practices
- Keep credentials and secrets out of source code (use secret manager/config)
- Use Docker Compose for scalable, reproducible grid runs
- Review Allure Reports after every run for actionable insights
- Add new tests using the Page Object Model for maintainability

## Troubleshooting
- If browsers do not launch, check Docker and Selenium Grid status
- If locators break, add fallback strategies to `LocatorUtil.selfHealing`
- For slow tests, tune explicit wait times in config
- For more parallelism, increase node count in `docker-compose.yml` and `gridExecutorCapacity`

## Contributors & License
- See `CONTRIBUTING.md` and `LICENSE` 
