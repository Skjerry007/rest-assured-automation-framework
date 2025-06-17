package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.AuthRequest;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

/**
 * AuthAPI - API methods for authentication endpoints
 */
public class AuthAPI extends BaseAPI {
    
    /**
     * Login with credentials
     * @param authRequest Auth request with email and password
     * @return Response with auth token
     */
    public Response login(AuthRequest authRequest) {
        LoggerUtil.info("Logging in with email: {}", authRequest.getEmail());
        return post(Endpoints.LOGIN, authRequest, null);
    }
    
    /**
     * Register new user
     * @param authRequest Auth request with registration details
     * @return Response with registration status
     */
    public Response register(AuthRequest authRequest) {
        LoggerUtil.info("Registering new user with email: {}", authRequest.getEmail());
        return post(Endpoints.REGISTER, authRequest, null);
    }
}