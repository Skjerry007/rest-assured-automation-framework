package com.restautomation.tests;

import com.restautomation.api.AlbumAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.Album;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * AlbumApiTest - Tests for Album API endpoints
 */
public class AlbumApiTest {
    private AlbumAPI albumAPI;
    
    @BeforeClass
    public void setup() {
        // Get AlbumAPI instance from factory
        albumAPI = APIFactory.getInstance().getAlbumAPI();
    }
    
    @Test(description = "Test getting all albums", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllAlbums() {
        // Get all albums
        Response response = albumAPI.getAllAlbums();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Albums list should not be empty");
        
        LoggerUtil.info("Retrieved {} albums", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting album by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAlbumById() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = albumAPI.getAlbumById(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", randomId);
        ResponseValidator.validateFieldExists(response, "userId");
        ResponseValidator.validateFieldExists(response, "title");
        String albumTitle = response.jsonPath().getString("title");
        LoggerUtil.info("Retrieved album with title: {}", albumTitle);
    }
    
    @Test(description = "Test creating a new album", retryAnalyzer = RetryAnalyzer.class)
    public void testCreateAlbum() {
        // Create album object
        Album album = Album.builder()
                .userId(1)
                .title("New Test Album")
                .build();
        
        // Create album
        Response response = albumAPI.createAlbum(album);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "title", "New Test Album");
        ResponseValidator.validateFieldValue(response, "userId", 1);
        
        LoggerUtil.info("Created album with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating an album", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateAlbum() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Album updatedAlbum = Album.builder()
                .userId(1)
                .title("Updated Album Title")
                .build();
        Response response = albumAPI.updateAlbum(randomId, updatedAlbum);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "title", "Updated Album Title");
        LoggerUtil.info("Updated album with ID: {}", randomId);
    }
    
    @Test(description = "Test deleting an album", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteAlbum() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = albumAPI.deleteAlbum(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted album with ID: {}", randomId);
    }
    
    @Test(description = "Test getting album photos", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAlbumPhotos() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 101); // 1 to 100 inclusive
        Response response = albumAPI.getAlbumPhotos(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Album photos should not be empty");
        LoggerUtil.info("Retrieved {} photos for album with ID: {}", response.jsonPath().getList("").size(), randomId);
    }
} 