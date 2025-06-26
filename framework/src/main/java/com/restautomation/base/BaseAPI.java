package com.restautomation.base;

import com.restautomation.config.ConfigManager;
import com.restautomation.utils.LoggerUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.config.ConnectionConfig.connectionConfig;
import static io.restassured.config.HttpClientConfig.httpClientConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * BaseAPI - Base class for all API requests
 */
public class BaseAPI {
    protected static final ConfigManager config = ConfigManager.getInstance();
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    
    /**
     * Initialize base specs
     */
    public BaseAPI() {
        initializeRequestSpec();
        initializeResponseSpec();
    }
    
    /**
     * Initialize request specification with common settings
     */
    private void initializeRequestSpec() {
    LoggerUtil.info("Initializing request specification");

    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
        .setBaseUri(config.getBaseUrl())
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON);

    // Set SSL relaxation if configured
    if (!config.isSslVerificationEnabled()) {
        requestSpecBuilder.setRelaxedHTTPSValidation();
    }

    // Set timeouts using proper config chaining
    RestAssured.config = RestAssured.config()
        .connectionConfig(connectionConfig())
        .httpClient(httpClientConfig()
            .setParam("http.connection.timeout", config.getTimeout() * 1000)
            .setParam("http.socket.timeout", config.getTimeout() * 1000));

    // Log request details
    requestSpecBuilder.log(LogDetail.ALL);

    requestSpec = requestSpecBuilder.build();
}
    
    /**
     * Initialize response specification with common settings
     */
    private void initializeResponseSpec() {
        LoggerUtil.info("Initializing response specification");
        
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        // Log response details
        responseSpecBuilder.log(LogDetail.ALL);
        
        responseSpec = responseSpecBuilder.build();
    }
    
    /**
     * Set headers for request specification
     * @param headers map of headers
     * @return RequestSpecification with headers
     */
    protected RequestSpecification setHeaders(Map<String, String> headers) {
        RequestSpecification reqSpec = RestAssured.given().spec(requestSpec);
        if (headers != null && !headers.isEmpty()) {
            reqSpec.headers(headers);
        }
        return reqSpec;
    }
    
    /**
     * Perform GET request
     * @param endpoint API endpoint
     * @param headers request headers
     * @return Response object
     */
    public Response get(String endpoint, Map<String, String> headers) {
        LoggerUtil.info("Performing GET request to: {}", endpoint);
        return setHeaders(headers)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform GET request with path parameters
     * @param endpoint API endpoint
     * @param pathParams path parameters
     * @param headers request headers
     * @return Response object
     */
    public Response get(String endpoint, Map<String, Object> pathParams, Map<String, String> headers) {
        LoggerUtil.info("Performing GET request to: {} with path params: {}", endpoint, pathParams);
        return setHeaders(headers)
                .pathParams(pathParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams query parameters
     * @param headers request headers
     * @return Response object
     */
    public Response getWithQueryParams(String endpoint, Map<String, Object> queryParams, Map<String, String> headers) {
        LoggerUtil.info("Performing GET request to: {} with query params: {}", endpoint, queryParams);
        return setHeaders(headers)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform POST request
     * @param endpoint API endpoint
     * @param requestBody request body
     * @param headers request headers
     * @return Response object
     */
    public Response post(String endpoint, Object requestBody, Map<String, String> headers) {
        LoggerUtil.info("Performing POST request to: {}", endpoint);
        RequestSpecification request = setHeaders(headers);
        if (requestBody != null) {
            request.body(requestBody);
        }
        return request
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform PUT request
     * @param endpoint API endpoint
     * @param requestBody request body
     * @param headers request headers
     * @return Response object
     */
    public Response put(String endpoint, Object requestBody, Map<String, String> headers) {
        LoggerUtil.info("Performing PUT request to: {}", endpoint);
        return setHeaders(headers)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform PUT request with path parameters
     * @param endpoint API endpoint
     * @param requestBody request body
     * @param pathParams path parameters
     * @param headers request headers
     * @return Response object
     */
    public Response put(String endpoint, Object requestBody, Map<String, Object> pathParams, Map<String, String> headers) {
        LoggerUtil.info("Performing PUT request to: {} with path params: {}", endpoint, pathParams);
        return setHeaders(headers)
                .pathParams(pathParams)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform DELETE request
     * @param endpoint API endpoint
     * @param headers request headers
     * @return Response object
     */
    public Response delete(String endpoint, Map<String, String> headers) {
        LoggerUtil.info("Performing DELETE request to: {}", endpoint);
        return setHeaders(headers)
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform DELETE request with path parameters
     * @param endpoint API endpoint
     * @param pathParams path parameters
     * @param headers request headers
     * @return Response object
     */
    public Response delete(String endpoint, Map<String, Object> pathParams, Map<String, String> headers) {
        LoggerUtil.info("Performing DELETE request to: {} with path params: {}", endpoint, pathParams);
        return setHeaders(headers)
                .pathParams(pathParams)
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform PATCH request
     * @param endpoint API endpoint
     * @param requestBody request body
     * @param headers request headers
     * @return Response object
     */
    public Response patch(String endpoint, Object requestBody, Map<String, String> headers) {
        LoggerUtil.info("Performing PATCH request to: {}", endpoint);
        return setHeaders(headers)
                .body(requestBody)
                .when()
                .patch(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
}