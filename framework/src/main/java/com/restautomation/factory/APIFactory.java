package com.restautomation.factory;

import com.restautomation.api.AuthAPI;
import com.restautomation.api.PostAPI;
import com.restautomation.api.ProductAPI;
import com.restautomation.api.UserAPI;
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
     * Get ProductAPI instance
     * @return ProductAPI instance
     */
    public ProductAPI getProductAPI() {
        LoggerUtil.info("Creating ProductAPI instance");
        return new ProductAPI();
    }
}