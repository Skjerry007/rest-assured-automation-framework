package com.restautomation.tests;

import com.restautomation.api.PostAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.Post;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.concurrent.ThreadLocalRandom;

/**
 * PostApiTest - Tests for Post API endpoints
 */
public class PostApiTest {
    private PostAPI postAPI;
    
    @BeforeClass
    public void setup() {
        // Get PostAPI instance from factory
        postAPI = APIFactory.getInstance().getPostAPI();
    }
    
    @Test(description = "Test getting all posts", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllPosts() {
        // Get all posts
        Response response = postAPI.getAllPosts();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        ResponseValidator.validateContentType(response, "application/json; charset=utf-8");
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Posts list should not be empty");
        
        LoggerUtil.info("Retrieved {} posts", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting post by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetPostById() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = postAPI.getPostById(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", randomId);
        ResponseValidator.validateFieldExists(response, "title");
        ResponseValidator.validateFieldExists(response, "body");
        ResponseValidator.validateFieldExists(response, "userId");
        String postTitle = response.jsonPath().getString("title");
        LoggerUtil.info("Retrieved post with title: {}", postTitle);
    }
    
    @Test(description = "Test creating a new post", retryAnalyzer = RetryAnalyzer.class)
    public void testCreatePost() {
        // Create post object
        Post post = Post.builder()
                .userId(1)
                .title("New Post Title")
                .body("This is the body of the new post")
                .build();
        
        // Create post
        Response response = postAPI.createPost(post);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "title", "New Post Title");
        ResponseValidator.validateFieldValue(response, "body", "This is the body of the new post");
        ResponseValidator.validateFieldValue(response, "userId", 1);
        
        LoggerUtil.info("Created post with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating a post", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdatePost() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Post updatedPost = Post.builder()
                .userId(1)
                .title("Updated Post Title")
                .body("This is the updated body of the post")
                .build();
        Response response = postAPI.updatePost(randomId, updatedPost);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "title", "Updated Post Title");
        ResponseValidator.validateFieldValue(response, "body", "This is the updated body of the post");
        LoggerUtil.info("Updated post with ID: {}", randomId);
    }
    
    @Test(description = "Test deleting a post", retryAnalyzer = RetryAnalyzer.class)
    public void testDeletePost() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = postAPI.deletePost(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted post with ID: {}", randomId);
    }
    
    @Test(description = "Test getting post comments", retryAnalyzer = RetryAnalyzer.class)
    public void testGetPostComments() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = postAPI.getPostComments(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Post comments should not be empty");
        LoggerUtil.info("Retrieved {} comments for post with ID: {}", response.jsonPath().getList("").size(), randomId);
    }
}