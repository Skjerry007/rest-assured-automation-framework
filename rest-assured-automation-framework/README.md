# REST Assured Automation Framework

A robust and comprehensive automation framework for testing RESTful APIs using REST Assured, TestNG, and Extent Reports, with Docker integration for CI/CD pipelines.

## Features

- **Design Patterns**
  - Singleton Pattern for configuration, reporting, and factory classes
  - Factory Pattern for API instance creation
  - Builder Pattern for models using Lombok
  - Page Object Model (POM) approach for API endpoints

- **Reporting**
  - Extent Reports integration for detailed HTML reports
  - TestNG listeners for test execution events
  - Custom logging with Log4j2

- **API Testing Capabilities**
  - HTTP method support: GET, POST, PUT, PATCH, DELETE
  - Request/response validation
  - JSON schema validation
  - Response time validation
  - Header and content type validation
  - SSL handling

- **Mock API Testing**
  - WireMock integration for mock API testing
  - Configurable mock responses

- **Security**
  - AWS Secret Manager integration for secure credentials
  - Token-based authentication

- **Exception Handling**
  - Custom exceptions
  - Retry mechanism for flaky tests

- **Docker Integration**
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
│   ├── test/java/com/restautomation
│   │   └── tests           # Test classes
│   └── test/resources
│       ├── config          # Environment configuration
│       ├── testdata        # Test data
│       └── testng.xml      # TestNG configuration
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

### Running Tests

Run all tests:
```
mvn test
```

Run specific test class:
```
mvn test -Dtest=UserApiTest
```

Run tests with specific environment:
```
mvn test -Denv=qa
```

### Docker Execution

Build Docker image:
```
docker build -t rest-automation .
```

Run tests in container:
```
docker run rest-automation
```

## Configuration

The framework supports multiple environments through property files:

- `src/test/resources/config/dev-config.properties` - Development environment
- `src/test/resources/config/qa-config.properties` - QA environment

## Adding New Tests

1. Create API endpoint class in `src/main/java/com/restautomation/api`
2. Add model class in `src/main/java/com/restautomation/models`
3. Add endpoint constants in `src/main/java/com/restautomation/constants/Endpoints.java`
4. Create test class in `src/test/java/com/restautomation/tests`

## Best Practices

- Follow the established design patterns
- Add proper logging for all API interactions
- Use builder pattern for request objects
- Validate all responses thoroughly
- Handle exceptions and add retries for flaky tests
- Use appropriate assertions for validations

## License

This project is licensed under the MIT License.