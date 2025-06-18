package com.restautomation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.Photo;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * PhotoAPI - API methods for photo endpoints
 */
public class PhotoAPI extends BaseAPI {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Get all photos
     * @return Response with all photos
     */
    public Response getAllPhotos() {
        LoggerUtil.info("Getting all photos");
        return get(Endpoints.PHOTOS, null);
    }
    
    /**
     * Get photo by ID
     * @param photoId photo ID
     * @return Response with photo details
     */
    public Response getPhotoById(int photoId) {
        LoggerUtil.info("Getting photo with ID: {}", photoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", photoId);
        return get(Endpoints.PHOTO_BY_ID, pathParams, null);
    }
    
    /**
     * Create new photo
     * @param photo Photo object
     * @return Response with created photo
     */
    public Response createPhoto(Photo photo) {
        LoggerUtil.info("Creating new photo: {}", photo);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return post(Endpoints.PHOTOS, photo, headers);
    }
    
    /**
     * Update photo
     * @param photoId photo ID
     * @param photo Updated Photo object
     * @return Response with updated photo
     */
    public Response updatePhoto(int photoId, Photo photo) {
        LoggerUtil.info("Updating photo with ID: {}", photoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", photoId);
        return put(Endpoints.PHOTO_BY_ID, photo, pathParams, null);
    }
    
    /**
     * Delete photo
     * @param photoId photo ID
     * @return Response with deletion status
     */
    public Response deletePhoto(int photoId) {
        LoggerUtil.info("Deleting photo with ID: {}", photoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", photoId);
        return delete(Endpoints.PHOTO_BY_ID, pathParams, null);
    }
} 