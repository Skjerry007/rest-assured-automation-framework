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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * ProductApiTest - Tests for Product API endpoints
 */
public class ProductApiTest {
    private ProductAPI productAPI;
    
    @BeforeClass
    public void setup() {
        // Get ProductAPI instance from factory
        productAPI = APIFactory.getInstance().getProductAPI();
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
        // Get product with ID 1
        Response response = productAPI.getProductById(1);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", 1);
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
        // Update product with ID 1
        Product updatedProduct = Product.builder()
                .name("Updated Product Name")
                .description("This is an updated product description")
                .price(new BigDecimal("39.99"))
                .stock(50)
                .category("Electronics")
                .build();
        
        // Update product
        Response response = productAPI.updateProduct(1, updatedProduct);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "name", "Updated Product Name");
        ResponseValidator.validateFieldValue(response, "description", "This is an updated product description");
        
        LoggerUtil.info("Updated product with ID: 1");
    }
    
    @Test(description = "Test deleting a product", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteProduct() {
        // Delete product with ID 1
        Response response = productAPI.deleteProduct(1);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        
        LoggerUtil.info("Deleted product with ID: 1");
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