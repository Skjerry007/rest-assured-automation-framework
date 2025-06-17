package com.restautomation.constants;

/**
 * StatusCodes - HTTP status code constants
 */
public class StatusCodes {
    // Success codes
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;
    
    // Client error codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int TOO_MANY_REQUESTS = 429;
    
    // Server error codes
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    
    private StatusCodes() {
        // Private constructor to prevent instantiation
    }
}