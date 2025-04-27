package com.restautomation.constants;

/**
 * Endpoints - Constants for API endpoints
 */
public class Endpoints {
    // Base endpoints
    public static final String USERS = "/users";
    public static final String POSTS = "/posts";
    public static final String COMMENTS = "/comments";
    public static final String PRODUCTS = "/products";
    
    // User endpoints
    public static final String USER_BY_ID = USERS + "/{id}";
    public static final String USER_POSTS = USER_BY_ID + "/posts";
    
    // Post endpoints
    public static final String POST_BY_ID = POSTS + "/{id}";
    public static final String POST_COMMENTS = POST_BY_ID + "/comments";
    
    // Auth endpoints
    public static final String AUTH = "/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String REGISTER = AUTH + "/register";
    
    // Products endpoints
    public static final String PRODUCT_BY_ID = PRODUCTS + "/{id}";
    
    private Endpoints() {
        // Private constructor to prevent instantiation
    }
}