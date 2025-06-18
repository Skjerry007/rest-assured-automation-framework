package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.User;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
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
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", userId);
        return get(Endpoints.USER_BY_ID, pathParams, null);
    }
    
    /**
     * Create new user
     * @param user User object
     * @return Response with created user
     */
    public Response createUser(User user) {
        LoggerUtil.info("Creating new user: {}", user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return post(Endpoints.USERS, user, headers);
    }
    
    /**
     * Update user
     * @param userId user ID
     * @param user Updated User object
     * @return Response with updated user
     */
    public Response updateUser(int userId, User user) {
        LoggerUtil.info("Updating user with ID: {}", userId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", userId);
        return put(Endpoints.USER_BY_ID, user, pathParams, null);
    }
    
    /**
     * Delete user
     * @param userId user ID
     * @return Response with deletion status
     */
    public Response deleteUser(int userId) {
        LoggerUtil.info("Deleting user with ID: {}", userId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", userId);
        return delete(Endpoints.USER_BY_ID, pathParams, null);
    }
    
    /**
     * Get posts by user ID
     * @param userId user ID
     * @return Response with user's posts
     */
    public Response getUserPosts(int userId) {
        LoggerUtil.info("Getting posts for user ID: {}", userId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", userId);
        return get(Endpoints.USER_POSTS, pathParams, null);
    }
}