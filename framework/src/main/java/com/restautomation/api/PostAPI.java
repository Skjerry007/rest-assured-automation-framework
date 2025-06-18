package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.Post;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PostAPI - API methods for post endpoints
 */
public class PostAPI extends BaseAPI {
    
    /**
     * Get all posts
     * @return Response with all posts
     */
    public Response getAllPosts() {
        LoggerUtil.info("Getting all posts");
        return get(Endpoints.POSTS, null);
    }
    
    /**
     * Get post by ID
     * @param postId post ID
     * @return Response with post details
     */
    public Response getPostById(int postId) {
        LoggerUtil.info("Getting post with ID: {}", postId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", postId);
        return get(Endpoints.POST_BY_ID, pathParams, null);
    }
    
    /**
     * Create new post
     * @param post Post object
     * @return Response with created post
     */
    public Response createPost(Post post) {
        LoggerUtil.info("Creating new post: {}", post);
        return post(Endpoints.POSTS, post, null);
    }
    
    /**
     * Update post
     * @param postId post ID
     * @param post Updated Post object
     * @return Response with updated post
     */
    public Response updatePost(int postId, Post post) {
        LoggerUtil.info("Updating post with ID: {}", postId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", postId);
        return put(Endpoints.POST_BY_ID, post, pathParams, null);
    }
    
    /**
     * Delete post
     * @param postId post ID
     * @return Response with deletion status
     */
    public Response deletePost(int postId) {
        LoggerUtil.info("Deleting post with ID: {}", postId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", postId);
        return delete(Endpoints.POST_BY_ID, pathParams, null);
    }
    
    /**
     * Get comments by post ID
     * @param postId post ID
     * @return Response with post's comments
     */
    public Response getPostComments(int postId) {
        LoggerUtil.info("Getting comments for post ID: {}", postId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", postId);
        return get(Endpoints.POST_COMMENTS, pathParams, null);
    }
}