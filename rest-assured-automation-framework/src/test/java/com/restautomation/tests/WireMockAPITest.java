package com.restautomation.tests;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.models.User;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.TestDataUtil;

import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wiremock.client.WireMock;
import org.wiremock.client.WireMock.*;

import java.util.HashMap;
import java.util.Map;

import static org.wiremock.client.WireMock.*;

/**
 * WireMockAPITest - Tests using WireMock for mocking API responses
 */
public class WireMockAPITest {
    private static final int PORT = 8989;
    private static final String HOST = "localhost";
    private WireMock wireMock;
    private BaseAPI api;
    
    @BeforeClass
    public void setup() {
        // Initialize WireMock server
        LoggerUtil.info("Setting up WireMock server on port: {}", PORT);
        wireMock = new WireMock(HOST, PORT);
        configureFor(HOST, PORT);
        
        // Initialize API with WireMock URL
        api = new BaseAPI() {
            {
                // Override base URL to point to WireMock server
                requestSpec = requestSpec.baseUri("http://" + HOST + ":" + PORT);
            }
        };
        
        // Start WireMock server
        WireMock.start();
    }
    
    @AfterClass
    public void tearDown() {
        // Stop WireMock server
        LoggerUtil.info("Stopping WireMock server");
        WireMock.stop();
    }
    
    @Test(description = "Test GET request with WireMock")
    public void testMockGetRequest() {
        // Setup mock response for GET request
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john@example.com\"}")));
        
        // Send GET request to WireMock server
        Response response = api.get("/users/1", null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "id", 1);
        ResponseValidator.validateFieldValue(response, "name", "John Doe");
        
        LoggerUtil.info("Successfully mocked GET request: {}", response.asString());
        
        // Verify that the expected request was made
        verify(getRequestedFor(urlEqualTo("/users/1")));
    }
    
    @Test(description = "Test POST request with WireMock")
    public void testMockPostRequest() {
        // Setup mock response for POST request
        stubFor(post(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 101, \"name\": \"New User\", \"email\": \"new@example.com\"}")));
        
        // Create user object
        User user = User.builder()
                .name("New User")
                .email("new@example.com")
                .build();
        
        // Send POST request to WireMock server
        Response response = api.post("/users", user, null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldValue(response, "id", 101);
        ResponseValidator.validateFieldValue(response, "name", "New User");
        
        LoggerUtil.info("Successfully mocked POST request: {}", response.asString());
        
        // Verify that the expected request was made
        verify(postRequestedFor(urlEqualTo("/users")));
    }
    
    @Test(description = "Test PUT request with WireMock")
    public void testMockPutRequest() {
        // Setup mock response for PUT request
        stubFor(put(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"Updated User\", \"email\": \"updated@example.com\"}")));
        
        // Create user object
        User user = User.builder()
                .name("Updated User")
                .email("updated@example.com")
                .build();
        
        // Send PUT request to WireMock server
        Response response = api.put("/users/1", user, null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "id", 1);
        ResponseValidator.validateFieldValue(response, "name", "Updated User");
        
        LoggerUtil.info("Successfully mocked PUT request: {}", response.asString());
        
        // Verify that the expected request was made
        verify(putRequestedFor(urlEqualTo("/users/1")));
    }
    
    @Test(description = "Test DELETE request with WireMock")
    public void testMockDeleteRequest() {
        // Setup mock response for DELETE request
        stubFor(delete(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"success\": true, \"message\": \"User deleted\"}")));
        
        // Send DELETE request to WireMock server
        Response response = api.delete("/users/1", null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "success", true);
        ResponseValidator.validateFieldValue(response, "message", "User deleted");
        
        LoggerUtil.info("Successfully mocked DELETE request: {}", response.asString());
        
        // Verify that the expected request was made
        verify(deleteRequestedFor(urlEqualTo("/users/1")));
    }
    
    @Test(description = "Test error response with WireMock")
    public void testMockErrorResponse() {
        // Setup mock error response
        stubFor(get(urlEqualTo("/users/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"User not found\", \"code\": 404}")));
        
        // Send GET request to WireMock server
        Response response = api.get("/users/999", null);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.NOT_FOUND);
        ResponseValidator.validateFieldValue(response, "error", "User not found");
        ResponseValidator.validateFieldValue(response, "code", 404);
        
        LoggerUtil.info("Successfully mocked error response: {}", response.asString());
        
        // Verify that the expected request was made
        verify(getRequestedFor(urlEqualTo("/users/999")));
    }
}