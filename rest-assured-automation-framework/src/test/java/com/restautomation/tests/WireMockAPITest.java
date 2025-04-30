package com.restautomation.tests;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.models.User;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import io.restassured.response.Response;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockAPITest {
    private static final int PORT = 8989;
    private static final String HOST = "localhost";
    private WireMockServer wireMockServer;
    private BaseAPI api;

    @BeforeClass
    public void setup() {
        // Initialize WireMock server
        LoggerUtil.info("Starting WireMock server on port: {}", PORT);
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(PORT));
        wireMockServer.start();

        // Configure client
        configureFor(HOST, PORT);

        // Initialize API with WireMock URL
        api = new BaseAPI() {{
            requestSpec = requestSpec.baseUri("http://" + HOST + ":" + PORT);
        }};
    }

    @AfterClass
    public void tearDown() {
        LoggerUtil.info("Stopping WireMock server");
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test(description = "Test GET request with WireMock")
    public void testMockGetRequest() {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john@example.com\"}")));

        Response response = api.get("/users/1", null);

        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "id", 1);
        ResponseValidator.validateFieldValue(response, "name", "John Doe");

        LoggerUtil.info("Successfully mocked GET request: {}", response.asString());
        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test(description = "Test POST request with WireMock")
    public void testMockPostRequest() {
        stubFor(post(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 101, \"name\": \"New User\", \"email\": \"new@example.com\"}")));

        User user = User.builder()
                .name("New User")
                .email("new@example.com")
                .build();

        Response response = api.post("/users", user, null);

        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldValue(response, "id", 101);
        ResponseValidator.validateFieldValue(response, "name", "New User");

        LoggerUtil.info("Successfully mocked POST request: {}", response.asString());
        verify(postRequestedFor(urlEqualTo("/users")));
    }

    @Test(description = "Test PUT request with WireMock")
    public void testMockPutRequest() {
        stubFor(put(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"Updated User\", \"email\": \"updated@example.com\"}")));

        User user = User.builder()
                .name("Updated User")
                .email("updated@example.com")
                .build();

        Response response = api.put("/users/1", user, null);

        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "id", 1);
        ResponseValidator.validateFieldValue(response, "name", "Updated User");

        LoggerUtil.info("Successfully mocked PUT request: {}", response.asString());
        verify(putRequestedFor(urlEqualTo("/users/1")));
    }

    @Test(description = "Test DELETE request with WireMock")
    public void testMockDeleteRequest() {
        stubFor(delete(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"success\": true, \"message\": \"User deleted\"}")));

        Response response = api.delete("/users/1", null);

        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "success", true);
        ResponseValidator.validateFieldValue(response, "message", "User deleted");

        LoggerUtil.info("Successfully mocked DELETE request: {}", response.asString());
        verify(deleteRequestedFor(urlEqualTo("/users/1")));
    }

    @Test(description = "Test error response with WireMock")
    public void testMockErrorResponse() {
        stubFor(get(urlEqualTo("/users/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"User not found\", \"code\": 404}")));

        Response response = api.get("/users/999", null);

        ResponseValidator.validateStatusCode(response, StatusCodes.NOT_FOUND);
        ResponseValidator.validateFieldValue(response, "error", "User not found");
        ResponseValidator.validateFieldValue(response, "code", 404);

        LoggerUtil.info("Successfully mocked error response: {}", response.asString());
        verify(getRequestedFor(urlEqualTo("/users/999")));
    }
}
