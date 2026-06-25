package api.constants;

/**
 * Endpoints - Constants for API endpoints
 */
public class Endpoints {
    // Base endpoints
    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/users/{id}";
    public static final String USER_POSTS = "/users/{id}/posts";

    private Endpoints() {
        // Private constructor to prevent instantiation
    }
}