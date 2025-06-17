# REST Assured & Selenium Automation Framework

A robust and comprehensive automation framework for testing RESTful APIs using REST Assured, and web UIs using Selenium WebDriver, with TestNG, Extent Reports, and Docker integration for CI/CD pipelines.

## Features

- **Design Patterns**
  - Singleton Pattern for configuration, reporting, and factory classes
  - Factory Pattern for API instance creation
  - Builder Pattern for models using Lombok
  - Page Object Model (POM) for both API endpoints and Selenium pages

- **Reporting**
  - Extent Reports for detailed HTML reports (API & Selenium)
  - TestNG listeners for test execution events
  - Custom logging with Log4j2

- **API Testing (REST Assured)**
  - HTTP method support: GET, POST, PUT, PATCH, DELETE
  - Request/response validation
  - JSON schema validation
  - Response time, header, and content type validation
  - SSL handling
  - Mock API testing with WireMock

- **Web UI Testing (Selenium)**
  - Page Object Model for maintainable UI tests
  - Custom Selenium keywords and utilities
  - Gmail OTP automation example

- **Security**
  - AWS Secret Manager integration for secure credentials
  - Token-based authentication

- **Exception Handling**
  - Custom exceptions
  - Retry mechanism for flaky tests

- **Docker & CI/CD**
  - Dockerfile for containerized execution
  - Jenkins pipeline integration

## Project Structure

```
├── src
│   ├── main/java/com/restautomation
│   │   ├── api             # API endpoint classes
│   │   ├── base            # Base classes
│   │   ├── config          # Configuration management
│   │   ├── constants       # Constants
│   │   ├── exceptions      # Custom exceptions
│   │   ├── factory         # Factory pattern
│   │   ├── listeners       # TestNG listeners
│   │   ├── models          # POJO classes
│   │   ├── reports         # Reporting utilities
│   │   ├── secretmanager   # AWS Secret Manager
│   │   └── utils           # Utility classes
│   ├── main/java/com/seleniumautomation
│   │   ├── classes         # Selenium keywords
│   │   ├── config          # Selenium config
│   │   ├── driver          # WebDriver setup
│   │   ├── locators        # Page locators
│   │   ├── pages           # Page Objects
│   │   └── utils           # Selenium utilities
│   ├── test/java/com/restautomation
│   │   └── tests           # API Test classes
│   ├── test/java/com/seleniumautomation
│   │   └── test            # Selenium Test classes
│   └── test/resources
│       ├── config          # Environment configuration
│       ├── testdata        # Test data
│       └── testng-*.xml    # TestNG configurations
├── Dockerfile              # Docker configuration
├── Jenkinsfile             # Jenkins pipeline
└── pom.xml                 # Maven configuration
```

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Docker (optional, for containerized execution)
- Jenkins (optional, for CI/CD)

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/rest-assured-automation-framework.git
   ```
2. Install dependencies:
   ```
   mvn clean install
   ```

## Running Tests

### Run All Tests (API + Selenium)
```
mvn test -Dtest=*Test
```

### Run Only REST Assured API Tests (using TestNG suite)
```
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-restassured.xml
```

### Run a Specific Test Class
```
mvn test -Dtest=UserApiTest
# or with full package
mvn test -Dtest=com.restautomation.tests.UserApiTest
```

### Run Selenium Tests (using TestNG suite)
```
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-selenium.xml
```

## Logs & Reports

- **Logs:** `logs/test-automation.log`
- **Surefire Reports:** `target/surefire-reports/`
- **Extent Reports:** `test-output/reports/` (files like `API-Test-Report-<timestamp>.html`)

## Troubleshooting Test Failures
- **Path Parameter Errors:** Ensure all required path parameters (e.g., `id`) are provided in test data/methods.
- **404 Errors:** The backend may not have the required data. Check if the test is negative (expected to fail) or if test data setup is needed.
- **Connection/Timeout Issues:** Ensure the API server is running and accessible.
- **WireMock/Mock Tests:** Ensure WireMock is configured and running if required.
- **Logs:** Check `logs/test-automation.log` for detailed error messages.

## Adding New Tests
- **API:**
  1. Create endpoint class in `src/main/java/com/restautomation/api`
  2. Add model in `src/main/java/com/restautomation/models`
  3. Add endpoint constants in `src/main/java/com/restautomation/constants/Endpoints.java`
  4. Create test class in `src/test/java/com/restautomation/tests`
- **Selenium:**
  1. Create Page Object in `src/main/java/com/seleniumautomation/pages`
  2. Add locators in `src/main/java/com/seleniumautomation/locators`
  3. Create test class in `src/test/java/com/seleniumautomation/test`

## Best Practices
- Follow the established design patterns
- Add proper logging for all API/UI interactions
- Use builder pattern for request objects
- Validate all responses and UI states thoroughly
- Handle exceptions and add retries for flaky tests
- Use appropriate assertions for validations

## License

This project is licensed under the MIT License.