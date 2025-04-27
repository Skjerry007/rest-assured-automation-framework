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
        // Get user with ID 1
        Response response = userAPI.getUserById(1);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", 1);
        ResponseValidator.validateFieldExists(response, "name");
        ResponseValidator.validateFieldExists(response, "email");
        
        String userName = response.jsonPath().getString("name");
        LoggerUtil.info("Retrieved user with name: {}", userName);
    }
    
    @Test(description = "Test creating a new user", retryAnalyzer = RetryAnalyzer.class)
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
        // Update user with ID 1
        User updatedUser = User.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .build();
        
        // Update user
        Response response = userAPI.updateUser(1, updatedUser);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "name", "Jane Smith");
        ResponseValidator.validateFieldValue(response, "email", "jane.smith@example.com");
        
        LoggerUtil.info("Updated user with ID: 1");
    }
    
    @Test(description = "Test deleting a user", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteUser() {
        // Delete user with ID 1
        Response response = userAPI.deleteUser(1);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        
        LoggerUtil.info("Deleted user with ID: 1");
    }
    
    @Test(description = "Test getting user posts", retryAnalyzer = RetryAnalyzer.class)
    public void testGetUserPosts() {
        // Get posts for user with ID 1
        Response response = userAPI.getUserPosts(1);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "User posts should not be empty");
        
        // Validate all posts have the correct user ID
        response.jsonPath().getList("").forEach(post -> {
            Map<String, Object> postMap = (Map<String, Object>) post;
            Assert.assertEquals(postMap.get("userId"), 1, "Post should have userId = 1");
        });
        
        LoggerUtil.info("Retrieved {} posts for user with ID: 1", response.jsonPath().getList("").size());
    }
}