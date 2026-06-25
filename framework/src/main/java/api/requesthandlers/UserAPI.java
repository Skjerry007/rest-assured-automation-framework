package api.requesthandlers;

import api.base.BaseAPI;
import api.constants.Endpoints;
import api.models.User;
import common.utils.LoggerUtil;
import io.restassured.response.Response;
import java.util.Map;

/**
 * UserAPI - API methods for user endpoints
 */
public class UserAPI extends BaseAPI {
    
    /**
     * Get all users
     * @return Response with all users
     */
    public Response getAllUsers() {
        LoggerUtil.info("Getting all users");
        return get(Endpoints.USERS, null);
    }
    
    /**
     * Get user by ID
     * @param userId user ID
     * @return Response with user details
     */
    public Response getUserById(int userId) {
        LoggerUtil.info("Getting user with ID: {}", userId);
        return get(Endpoints.USER_BY_ID, Map.of("id", userId), null);
    }
    
    /**
     * Create new user
     * @param user User object
     * @return Response with created user
     */
    public Response createUser(User user) {
        LoggerUtil.info("Creating new user: {}", user);
        return post(Endpoints.USERS, user, Map.of(
            "Content-Type", "application/json",
            "Accept", "application/json"
        ));
    }
    
    /**
     * Update user
     * @param userId user ID
     * @param user Updated User object
     * @return Response with updated user
     */
    public Response updateUser(int userId, User user) {
        LoggerUtil.info("Updating user with ID: {}", userId);
        return put(Endpoints.USER_BY_ID, user, Map.of("id", userId), null);
    }
    
    /**
     * Delete user
     * @param userId user ID
     * @return Response with deletion status
     */
    public Response deleteUser(int userId) {
        LoggerUtil.info("Deleting user with ID: {}", userId);
        return delete(Endpoints.USER_BY_ID, Map.of("id", userId), null);
    }
    
    /**
     * Get posts by user ID
     * @param userId user ID
     * @return Response with user's posts
     */
    public Response getUserPosts(int userId) {
        LoggerUtil.info("Getting posts for user ID: {}", userId);
        return get(Endpoints.USER_POSTS, Map.of("id", userId), null);
    }
}
