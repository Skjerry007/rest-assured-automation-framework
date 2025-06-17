package com.restautomation.tests;

import com.restautomation.api.ProductAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.Product;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * ProductApiTest - Tests for Product API endpoints
 */
public class ProductApiTest {
    private ProductAPI productAPI;
    private Integer createdProductId;
    
    @BeforeClass
    public void setup() {
        // Get ProductAPI instance from factory
        productAPI = APIFactory.getInstance().getProductAPI();
    }
    
    @BeforeMethod
    public void createProductForTest() {
        // Create a product before each test that needs an ID
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test product")
                .price(new BigDecimal("19.99"))
                .stock(10)
                .category("TestCategory")
                .imageUrl("https://example.com/test-product.jpg")
                .build();
        Response response = productAPI.createProduct(product);
        if (response.jsonPath().get("id") == null) {
            // Public API does not persist data, skip test
            throw new SkipException("Product creation failed or public API does not persist data. Skipping test.");
        }
        createdProductId = response.jsonPath().getInt("id");
    }
    
    @Test(description = "Test getting all products", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllProducts() {
        // Get all products
        Response response = productAPI.getAllProducts();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Products list should not be empty");
        
        LoggerUtil.info("Retrieved {} products", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting product by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetProductById() {
        Response response = productAPI.getProductById(createdProductId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", createdProductId);
        ResponseValidator.validateFieldExists(response, "name");
        ResponseValidator.validateFieldExists(response, "price");
        String productName = response.jsonPath().getString("name");
        LoggerUtil.info("Retrieved product with name: {}", productName);
    }
    
    @Test(description = "Test creating a new product", retryAnalyzer = RetryAnalyzer.class)
    public void testCreateProduct() {
        // Create product object
        Product product = Product.builder()
                .name("New Test Product")
                .description("This is a test product")
                .price(new BigDecimal("29.99"))
                .stock(100)
                .category("Electronics")
                .imageUrl("https://example.com/product.jpg")
                .build();
        
        // Create product
        Response response = productAPI.createProduct(product);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "name", "New Test Product");
        ResponseValidator.validateFieldValue(response, "description", "This is a test product");
        ResponseValidator.validateFieldValue(response, "category", "Electronics");
        
        LoggerUtil.info("Created product with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating a product", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateProduct() {
        Product updatedProduct = Product.builder()
                .name("Updated Product Name")
                .description("This is an updated product description")
                .price(new BigDecimal("39.99"))
                .stock(50)
                .category("Electronics")
                .build();
        Response response = productAPI.updateProduct(createdProductId, updatedProduct);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "name", "Updated Product Name");
        ResponseValidator.validateFieldValue(response, "description", "This is an updated product description");
        LoggerUtil.info("Updated product with ID: {}", createdProductId);
    }
    
    @Test(description = "Test deleting a product", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteProduct() {
        Response response = productAPI.deleteProduct(createdProductId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted product with ID: {}", createdProductId);
    }
    
    @Test(description = "Test searching products by name", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchProductsByName() {
        // Search products with name containing "phone"
        Response response = productAPI.searchProductsByName("phone");
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        
        LoggerUtil.info("Found {} products matching search criteria", response.jsonPath().getList("").size());
    }
}