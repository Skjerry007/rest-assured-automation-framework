package api.endpoints;

import api.base.BaseAPI;
import api.constants.Endpoints;
import api.models.AuthRequest;
import common.utils.LoggerUtil;
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