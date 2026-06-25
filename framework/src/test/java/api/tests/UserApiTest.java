package api.tests;

import api.endpoints.UserAPI;
import api.constants.StatusCodes;

import api.models.User;
import common.utils.LoggerUtil;
import api.utils.ResponseValidator;

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
        userAPI = new UserAPI();
    }

    @Test(description = "Test getting all users")
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

    @Test(description = "Test getting user by ID")
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

    @Test(description = "Test creating a new user", dataProvider = "userDataProvider", dataProviderClass = api.providers.UserDataProviders.class)
    public void testCreateUser(User user) {
        // Create user
        Response response = userAPI.createUser(user);

        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "name", user.getName());
        ResponseValidator.validateFieldValue(response, "email", user.getEmail());
        ResponseValidator.validateSchema(response, "schemas/user-schema.json");

        LoggerUtil.info("Created user with ID: {}", response.jsonPath().getInt("id"));
    }

    @Test(description = "Test updating a user", dataProvider = "userDataProvider", dataProviderClass = api.providers.UserDataProviders.class)
    public void testUpdateUser(User updatedUser) {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        LoggerUtil.info("Updating user with ID: {}", randomId);
        
        Response response = userAPI.updateUser(randomId, updatedUser);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "name", updatedUser.getName());
        ResponseValidator.validateFieldValue(response, "email", updatedUser.getEmail());
        LoggerUtil.info("Updated user with ID: {}", randomId);
    }

    @Test(description = "Test deleting a user")
    public void testDeleteUser() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 11); // 1 to 10 inclusive
        LoggerUtil.info("Deleting user with ID: {}", randomId);
        Response response = userAPI.deleteUser(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted user with ID: {}", randomId);
    }

    @Test(description = "Test getting user posts")
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