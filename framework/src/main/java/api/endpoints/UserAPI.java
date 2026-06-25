package api.endpoints;

import api.base.BaseAPI;
import api.constants.Endpoints;
import api.models.User;
import common.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * UserAPI - API methods for user endpoints
 */
public class UserAPI extends BaseAPI {

    /**
     * Get all users
     * 
     * @return Response with all users
     */
    public Response getAllUsers() {
        LoggerUtil.info("Getting all users");
        return get(Endpoints.USERS, null);
    }

    /**
     * Create new user
     * 
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

}