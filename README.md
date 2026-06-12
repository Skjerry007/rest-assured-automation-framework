# REST Assured & Selenium Test Automation Framework

This framework provides robust, maintainable, and scalable automation for both REST APIs and web UI (Selenium) with Dockerized Selenium Grid, security secret management, global default timeout strategies, structured logging, and beautiful Allure reporting.

## Features

### UI Automation (Naukri Example)
- **Full end-to-end automation** of Naukri resume upload workflow with OTP retrieval
- **Global Default Timeout Strategy**: Configured implicit waits, page load timeouts, and script timeouts globally at the driver initialization level
- **Thread-safe WebDriver management**: Robust parallel execution support
- **Retry mechanism**: TestNG retry analyzer dynamically applied to retry failed tests automatically
- **Comprehensive logging**: Structured Logging utilizing Log4j2 and descriptive try-catch block step logging
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

### Docker & Selenium Grid
- **docker-compose.yml** for easy Selenium Grid setup (hub + Chrome + Firefox nodes)
- Run UI tests in parallel across browsers/containers

---

## What's Included vs. What's Removed

To keep the automation framework clean, maintainable, and focused on robust core functionalities, several modules have been streamlined:

### 🟢 What is Present (Included)
- **Page Object Model (POM)**: Maintained clean separation of page interactions in `ui/pages/` and static element mappings in `ui/locators/NaukriLocators.java`.
- **Global Timeout Strategy**: Managed timeouts (implicit, page load, and script) centrally in [DriverManager.java](file:///Users/shashank/Desktop/Desktop - Shashank’s MacBook Air/rest-assured-automation-framework/framework/src/main/java/ui/driver/DriverManager.java) to eliminate redundant wait boilerplate.
- **REST API Automation Engine**: Comprehensive REST Assured clients, POJO response schemas, and schema validation.
- **WireMock Mock API Server**: Configured for local API verification testing under `WireMockAPITest.java`.
- **Log4j2 Structured Logging**: Explicit step-by-step try-catch logging within page object action methods.
- **Allure Report Integration**: Auto-capturing of screenshots on failure and step annotations.
- **Dockerized Selenium Grid**: Standard Selenium Hub and browser node orchestration (`docker-compose.yml`).

### 🔴 What is NOT Present (Removed)
- **CDP Network Interception**: Chrome DevTools Protocol network interception tests and scripts have been fully removed to maintain deterministic browser execution.
- **Self-Healing Locators (`LocatorUtil`)**: Replaced by standard page object locator definitions in `NaukriLocators.java` for simpler element maintenance.
- **SauceDemo E2E Flows**: Cleaned up to keep focus on the Naukri resume-upload end-to-end suite.

---

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
   - Edit the configuration properties file at: `framework/src/test/resources/config/config.properties`
   - Edit the credentials file at: `framework/src/test/resources/config/credentials.json`
5. **(Optional) Set up Secret Manager** for secure credentials (using `setup-secrets.sh`)

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
│   │   └── test/resources/             # Test resources
│   │       ├── config/                 # Test configuration
│   │       │   ├── config.properties   # Main environment configuration
│   │       │   └── credentials.json    # Google OAuth credentials
│   │       ├── schemas/                # JSON schemas
│   │       │   └── user-schema.json
│   │       ├── testdata/               # Test data files
│   │       │   ├── posts.json
│   │       │   └── users.json
│   │       ├── allure.properties       # Allure reporting configuration
│   │       ├── log4j2.xml              # Logging configuration
│   │       ├── rest-assured.properties # REST Assured configuration
│   │       ├── testng-restassured.xml  # TestNG suite for API tests
│   │       ├── testng-selenium.xml     # TestNG suite for UI tests
│   │       └── testng.xml              # Main TestNG suite
├── docker-compose.yml                  # Selenium Grid setup
├── README.md                           # This file
├── .gitignore                          # Git ignore rules
├── SECURITY.md                         # Security guidelines
└── setup-secrets.sh                    # Secret setup script
```

### Key Features of the Structure:

✅ **Flat Package Structure**: Refactored to root `common`, `api`, and `ui` packages under `src/main/java` and `src/test/java` without nested `com/` packages.

✅ **Single Maven POM**: Simplified structure with one complete `pom.xml` in the framework directory.

✅ **Page Object Model**: Clean separation of page objects, locators mapping, and test classes.

✅ **Modular Design**: Distinct layers for REST API and Selenium automation.

✅ **Comprehensive Testing**: Both API and UI tests with proper test organization.

✅ **Docker Support**: Ready-to-use Selenium Grid with Docker Compose.

---

## Running UI Tests

### **Locally:**
```bash
# Run all UI tests (TestNG suite)
mvn clean test -Dtestng.file=testng-selenium.xml -DskipTests=false -Dmaven.test.skip=false

# Run a specific test class
mvn test -Dtest=NaukriResumeUploadTest -DskipTests=false -Dmaven.test.skip=false
```

### **On Selenium Grid (Docker Compose):**
1. **Start the grid:**
   ```bash
   docker-compose up -d
   # Grid will be at http://localhost:4444
   ```
2. **Run tests on grid:**
   ```bash
   mvn clean test -DgridExecutorCapacity=2 -Dtestng.file=testng-selenium.xml -DskipTests=false -Dmaven.test.skip=false
   ```
   - Increase `gridExecutorCapacity` for more parallelism

---

## Running API Tests
```bash
# Run all API tests (TestNG suite)
mvn clean test -Dtestng.file=testng-restassured.xml -DskipTests=false -Dmaven.test.skip=false

# Run a specific API test class
mvn test -Dtest=UserApiTest -DskipTests=false -Dmaven.test.skip=false
```

---

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

---

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

---

## Naukri UI Automation Details
- **Tested Flows:**
  - Login page credentials input (positive/negative scenarios)
  - OTP retrieval via Gmail API (`GmailService`)
  - Resume upload flow page navigation
- **Global timeout strategy** manages waits implicitly at the driver creation stage
- **All credentials** are fetched dynamically from GCP/AWS Secret Manager or system properties
- **Full Log4j2 execution tracing** recorded per test method

---

## Best Practices
- Keep credentials and secrets out of source code (use secret manager/config)
- Use Docker Compose for scalable, reproducible grid runs
- Review Allure Reports after every run for actionable insights
- Add new tests using the Page Object Model for maintainability

---

## Troubleshooting
- If browsers do not launch, check Docker and Selenium Grid status.
- If UI locators break, update the corresponding locator definitions inside `ui/locators/NaukriLocators.java`.
- For slow tests, tune explicit wait times in `config.properties`.
- For more parallelism, increase node count in `docker-compose.yml` and `gridExecutorCapacity`.

---

## Contributors & License
- See `CONTRIBUTING.md` and `LICENSE`
