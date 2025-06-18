package com.restautomation.tests;

import com.restautomation.api.UserAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.User;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UserApiTest - Tests for User API endpoints
 */
public class UserApiTest {
    private UserAPI userAPI;
    
    @BeforeClass
    public void setup() {
        // Get UserAPI instance from factory
        userAPI = APIFactory.getInstance().getUserAPI();
    }
    
    @Test(description = "Test getting all users", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllUsers() {
        // Get all users
        Response response = userAPI.getAllUsers();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        ResponseValidator.validateContentType(response, "application/json; charset=utf-8");
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Users list should not be empty");
        
        LoggerUtil.info("Retrieved {} users", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting user by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserById() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        Response response = userAPI.getUserById(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", randomId);
        ResponseValidator.validateFieldExists(response, "name");
        ResponseValidator.validateFieldExists(response, "email");
        String userName = response.jsonPath().getString("name");
        LoggerUtil.info("Retrieved user with name: {}", userName);
    }
    
    @Test(description = "Test creating a new user", retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void testCreateUser() {
        // Create user object
        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .phone("1234567890")
                .website("johndoe.com")
                .build();
        
        // Create user
        Response response = userAPI.createUser(user);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "name", "John Doe");
        ResponseValidator.validateFieldValue(response, "email", "john.doe@example.com");
        
        LoggerUtil.info("Created user with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating a user", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateUser() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        LoggerUtil.info("Updating user with ID: {}", randomId);
        User updatedUser = User.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .build();
        Response response = userAPI.updateUser(randomId, updatedUser);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "name", "Jane Smith");
        ResponseValidator.validateFieldValue(response, "email", "jane.smith@example.com");
        LoggerUtil.info("Updated user with ID: {}", randomId);
    }
    
    @Test(description = "Test deleting a user", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteUser() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        LoggerUtil.info("Deleting user with ID: {}", randomId);
        Response response = userAPI.deleteUser(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted user with ID: {}", randomId);
    }
    
    @Test(description = "Test getting user posts", retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserPosts() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        Response response = userAPI.getUserPosts(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        Assert.assertTrue(response.jsonPath().getList("").size() >= 0, "User posts should not be empty");
        response.jsonPath().getList("").forEach(post -> {
            Map<String, Object> postMap = (Map<String, Object>) post;
            Assert.assertEquals(postMap.get("userId"), randomId, "Post should have userId = " + randomId);
        });
        LoggerUtil.info("Retrieved {} posts for user with ID: {}", response.jsonPath().getList("").size(), randomId);
    }
}