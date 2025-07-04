package com.restautomation.constants;

/**
 * Endpoints - Constants for API endpoints
 */
public class Endpoints {
    // Base endpoints
    public static final String USERS = "/users";
    public static final String POSTS = "/posts";
    public static final String COMMENTS = "/comments";
    public static final String ALBUMS = "/albums";
    public static final String PHOTOS = "/photos";
    public static final String TODOS = "/todos";
    
    // User endpoints
    public static final String USER_BY_ID = USERS + "/{id}";
    public static final String USER_POSTS = USER_BY_ID + "/posts";
    public static final String USER_ALBUMS = USER_BY_ID + "/albums";
    public static final String USER_TODOS = USER_BY_ID + "/todos";
    
    // Post endpoints
    public static final String POST_BY_ID = POSTS + "/{id}";
    public static final String POST_COMMENTS = POST_BY_ID + "/comments";
    
    // Album endpoints
    public static final String ALBUM_BY_ID = ALBUMS + "/{id}";
    public static final String ALBUM_PHOTOS = ALBUM_BY_ID + "/photos";
    
    // Photo endpoints
    public static final String PHOTO_BY_ID = PHOTOS + "/{id}";
    
    // Todo endpoints
    public static final String TODO_BY_ID = TODOS + "/{id}";
    
    // Auth endpoints
    public static final String AUTH = "/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String REGISTER = AUTH + "/register";
    
    private Endpoints() {
        // Private constructor to prevent instantiation
    }
}