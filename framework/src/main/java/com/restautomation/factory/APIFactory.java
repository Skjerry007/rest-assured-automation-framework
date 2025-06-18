package com.restautomation.factory;

import com.restautomation.api.AuthAPI;
import com.restautomation.api.PostAPI;
import com.restautomation.api.UserAPI;
import com.restautomation.api.AlbumAPI;
import com.restautomation.api.PhotoAPI;
import com.restautomation.api.TodoAPI;
import com.restautomation.utils.LoggerUtil;

/**
 * APIFactory - Factory class for creating API instances
 */
public class APIFactory {
    private static APIFactory instance;
    
    private APIFactory() {
        // Private constructor for singleton
    }
    
    /**
     * Get singleton instance
     * @return APIFactory instance
     */
    public static synchronized APIFactory getInstance() {
        if (instance == null) {
            instance = new APIFactory();
        }
        return instance;
    }
    
    /**
     * Get UserAPI instance
     * @return UserAPI instance
     */
    public UserAPI getUserAPI() {
        LoggerUtil.info("Creating UserAPI instance");
        return new UserAPI();
    }
    
    /**
     * Get PostAPI instance
     * @return PostAPI instance
     */
    public PostAPI getPostAPI() {
        LoggerUtil.info("Creating PostAPI instance");
        return new PostAPI();
    }
    
    /**
     * Get AuthAPI instance
     * @return AuthAPI instance
     */
    public AuthAPI getAuthAPI() {
        LoggerUtil.info("Creating AuthAPI instance");
        return new AuthAPI();
    }
    
    /**
     * Get AlbumAPI instance
     * @return AlbumAPI instance
     */
    public AlbumAPI getAlbumAPI() {
        LoggerUtil.info("Creating AlbumAPI instance");
        return new AlbumAPI();
    }
    
    /**
     * Get PhotoAPI instance
     * @return PhotoAPI instance
     */
    public PhotoAPI getPhotoAPI() {
        LoggerUtil.info("Creating PhotoAPI instance");
        return new PhotoAPI();
    }
    
    /**
     * Get TodoAPI instance
     * @return TodoAPI instance
     */
    public TodoAPI getTodoAPI() {
        LoggerUtil.info("Creating TodoAPI instance");
        return new TodoAPI();
    }
}