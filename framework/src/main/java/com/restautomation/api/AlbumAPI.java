package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.Album;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * AlbumAPI - API methods for album endpoints
 */
public class AlbumAPI extends BaseAPI {
    
    /**
     * Get all albums
     * @return Response with all albums
     */
    public Response getAllAlbums() {
        LoggerUtil.info("Getting all albums");
        return get(Endpoints.ALBUMS, null);
    }
    
    /**
     * Get album by ID
     * @param albumId album ID
     * @return Response with album details
     */
    public Response getAlbumById(int albumId) {
        LoggerUtil.info("Getting album with ID: {}", albumId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", albumId);
        return get(Endpoints.ALBUM_BY_ID, pathParams, null);
    }
    
    /**
     * Create new album
     * @param album Album object
     * @return Response with created album
     */
    public Response createAlbum(Album album) {
        LoggerUtil.info("Creating new album: {}", album);
        return post(Endpoints.ALBUMS, album, null);
    }
    
    /**
     * Update album
     * @param albumId album ID
     * @param album Updated Album object
     * @return Response with updated album
     */
    public Response updateAlbum(int albumId, Album album) {
        LoggerUtil.info("Updating album with ID: {}", albumId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", albumId);
        return put(Endpoints.ALBUM_BY_ID, album, pathParams, null);
    }
    
    /**
     * Delete album
     * @param albumId album ID
     * @return Response with deletion status
     */
    public Response deleteAlbum(int albumId) {
        LoggerUtil.info("Deleting album with ID: {}", albumId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", albumId);
        return delete(Endpoints.ALBUM_BY_ID, pathParams, null);
    }
    
    /**
     * Get photos by album ID
     * @param albumId album ID
     * @return Response with album's photos
     */
    public Response getAlbumPhotos(int albumId) {
        LoggerUtil.info("Getting photos for album ID: {}", albumId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", albumId);
        return get(Endpoints.ALBUM_PHOTOS, pathParams, null);
    }
} 