# REST Assured & Selenium Automation Framework

This framework is designed for automated testing of both REST APIs and web applications using REST Assured and Selenium WebDriver.

## Features

### Design Patterns
- Singleton Pattern for configuration, reporting, and factory classes
- Factory Pattern for API instance creation
- Builder Pattern for models using Lombok
- Page Object Model (POM) for both API endpoints and Selenium pages

### Reporting
- Extent Reports for detailed HTML reports (API & Selenium)
- TestNG listeners for test execution events
- Custom logging with Log4j2

### REST Assured Features
- REST API testing with REST Assured
- HTTP method support: GET, POST, PUT, PATCH, DELETE
- Request/response validation
- JSON Schema validation
- Response time, header, and content type validation
- SSL handling
- Mock API testing with WireMock
- Authentication handling (Basic, OAuth, JWT)
- Environment-specific configurations
- Test data management
- API response validation
- Custom reporting

### Selenium Features
- Web UI testing with Selenium WebDriver
- Page Object Model (POM) design pattern
- Custom Selenium keywords and utilities
- Gmail OTP automation example
- CAPTCHA handling with Tesseract OCR
- Environment-specific configurations (dev/qa)
- Utility classes for common operations

### Security
- AWS Secret Manager integration for secure credentials
- Google Secret Manager integration
- Token-based authentication
- Environment variable management
- Secure credential storage

### Exception Handling
- Custom exceptions
- Retry mechanism for flaky tests
- Comprehensive error logging
- Test failure screenshots

### Docker & CI/CD
- Dockerfile for containerized execution
- Jenkins pipeline integration
- Cross-browser testing support
- Parallel test execution

## Prerequisites

- Java JDK 8 or higher
- Maven
- Chrome WebDriver
- Tesseract OCR (for CAPTCHA handling)
- Docker (optional, for containerized execution)
- Jenkins (optional, for CI/CD)

## Installation

1. Clone the repository
2. Install Tesseract OCR:
   ```bash
   # For macOS
   brew install tesseract
   
   # For Ubuntu/Debian
   sudo apt-get install tesseract-ocr
   ```

3. Install Maven dependencies:
   ```bash
   mvn clean install
   ```

## Environment Setup

1. Create a `.env` file in the root directory:
   ```bash
   cp .env.example .env
   ```

2. Update the `.env` file with your environment-specific values:
   - API Configuration
   - Web Configuration
   - Authentication settings
   - Secret Manager Configuration
   - Test Credentials
   - Gmail Configuration
   - Logging settings
   - Test Configuration

3. Set up Secret Management:
   - For AWS Secrets Manager:
     ```bash
     # Install AWS CLI
     brew install awscli
     
     # Configure AWS credentials
     aws configure
     ```
   
   - For Google Secret Manager:
     ```bash
     # Install Google Cloud SDK
     brew install google-cloud-sdk
     
     # Initialize and configure
     gcloud init
     ```

4. Create configuration files:
   ```bash
   # Copy example configurations
   cp framework/src/test/resources/config/dev-config.example.properties framework/src/test/resources/config/dev-config.properties
   cp framework/src/test/resources/config/qa-config.example.properties framework/src/test/resources/config/qa-config.properties
   
   # Update the configuration files with your environment-specific values
   ```

## Project Structure

```
framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── seleniumautomation/
│   │   │   │   │   ├── base/
│   │   │   │   │   ├── locators/
│   │   │   │   │   ├── pages/
│   │   │   │   │   └── utils/
│   │   │   │   └── restautomation/
│   │   │   │       ├── api/
│   │   │   │       ├── models/
│   │   │   │       ├── utils/
│   │   │   │       └── factory/
│   │   │   └── resources/
│   │   └── webapp/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       ├── seleniumautomation/
│       │       │   └── test/
│       │       └── restautomation/
│       │           └── tests/
│       └── resources/
│           ├── config/
│           ├── testdata/
│           └── testng-selenium.xml
└── pom.xml
```

## Configuration

- Environment-specific configurations are in `src/test/resources/config/`
- Locator properties are in `src/main/java/com/seleniumautomation/locators/`
- TestNG configuration is in `src/test/resources/testng-selenium.xml`
- API test data is in `src/test/resources/testdata/`

## Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=AmazonSearchTest
mvn test -Dtest=UserApiTest

# Run specific test suite
mvn test -Dsuite=api-tests
mvn test -Dsuite=ui-tests

# Run tests in Docker
docker build -t automation-framework .
docker run automation-framework
```

## Key Components

### REST Assured Components
- **BaseAPI**: Base class for all API test classes
- **APIFactory**: Factory class for creating API instances
- **AuthAPI**: API methods for authentication endpoints
- **UserAPI**: API methods for user management
- **ProductAPI**: API methods for product management
- **JWTUtil**: Utility for JWT token generation and validation
- **WireMockServer**: Mock API server for testing
- **ResponseValidator**: Custom response validation utilities

### Selenium Components
- **BaseTest**: Base class for all test classes
- **AmazonHomePage**: Page object for Amazon homepage
- **AmazonSearchResultsPage**: Page object for search results
- **AmazonProductPage**: Page object for product details
- **CaptchaReader**: Utility for handling CAPTCHA using Tesseract OCR
- **SeleniumKeywords**: Custom Selenium wrapper methods
- **GmailService**: Gmail OTP automation utility
- **ScreenshotUtil**: Test failure screenshot utility

## Security

Please refer to [SECURITY.md](SECURITY.md) for security guidelines and best practices.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 