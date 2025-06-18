package com.restautomation.tests;

import com.restautomation.api.PhotoAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.Photo;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * PhotoApiTest - Tests for Photo API endpoints
 */
public class PhotoApiTest {
    private PhotoAPI photoAPI;
    
    @BeforeClass
    public void setup() {
        // Get PhotoAPI instance from factory
        photoAPI = APIFactory.getInstance().getPhotoAPI();
    }
    
    @Test(description = "Test getting all photos", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllPhotos() {
        // Get all photos
        Response response = photoAPI.getAllPhotos();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Photos list should not be empty");
        
        LoggerUtil.info("Retrieved {} photos", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting photo by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetPhotoById() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 5001); // 1 to 5000 inclusive
        Response response = photoAPI.getPhotoById(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", randomId);
        ResponseValidator.validateFieldExists(response, "albumId");
        ResponseValidator.validateFieldExists(response, "title");
        ResponseValidator.validateFieldExists(response, "url");
        ResponseValidator.validateFieldExists(response, "thumbnailUrl");
        String photoTitle = response.jsonPath().getString("title");
        LoggerUtil.info("Retrieved photo with title: {}", photoTitle);
    }
    
    @Test(description = "Test creating a new photo", retryAnalyzer = RetryAnalyzer.class, enabled = false)
    public void testCreatePhoto() {
        // Create photo object
        Photo photo = Photo.builder()
                .albumId(1)
                .title("New Test Photo")
                .url("https://example.com/photo.jpg")
                .thumbnailUrl("https://example.com/photo_thumb.jpg")
                .build();
        
        // Create photo
        Response response = photoAPI.createPhoto(photo);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "title", photo.getTitle());
        ResponseValidator.validateFieldValue(response, "albumId", photo.getAlbumId());
        ResponseValidator.validateFieldValue(response, "url", photo.getUrl());
        ResponseValidator.validateFieldValue(response, "thumbnailUrl", photo.getThumbnailUrl());
        
        LoggerUtil.info("Created photo with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating a photo", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdatePhoto() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 5001); // 1 to 5000 inclusive
        Photo updatedPhoto = Photo.builder()
                .albumId(1)
                .title("Updated Photo Title")
                .url("https://example.com/updated_photo.jpg")
                .thumbnailUrl("https://example.com/updated_photo_thumb.jpg")
                .build();
        Response response = photoAPI.updatePhoto(randomId, updatedPhoto);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "title", "Updated Photo Title");
        LoggerUtil.info("Updated photo with ID: {}", randomId);
    }
    
    @Test(description = "Test deleting a photo", retryAnalyzer = RetryAnalyzer.class)
    public void testDeletePhoto() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 5001); // 1 to 5000 inclusive
        Response response = photoAPI.deletePhoto(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted photo with ID: {}", randomId);
    }
} 