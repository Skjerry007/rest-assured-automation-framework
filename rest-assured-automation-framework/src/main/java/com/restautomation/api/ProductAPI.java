package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.Product;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ProductAPI - API methods for product endpoints
 */
public class ProductAPI extends BaseAPI {
    
    /**
     * Get all products
     * @return Response with all products
     */
    public Response getAllProducts() {
        LoggerUtil.info("Getting all products");
        return get(Endpoints.PRODUCTS, null);
    }
    
    /**
     * Get product by ID
     * @param productId product ID
     * @return Response with product details
     */
    public Response getProductById(int productId) {
        LoggerUtil.info("Getting product with ID: {}", productId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", productId);
        return get(Endpoints.PRODUCT_BY_ID, pathParams, null);
    }
    
    /**
     * Create new product
     * @param product Product object
     * @return Response with created product
     */
    public Response createProduct(Product product) {
        LoggerUtil.info("Creating new product: {}", product);
        return post(Endpoints.PRODUCTS, product, null);
    }
    
    /**
     * Update product
     * @param productId product ID
     * @param product Updated Product object
     * @return Response with updated product
     */
    public Response updateProduct(int productId, Product product) {
        LoggerUtil.info("Updating product with ID: {}", productId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", productId);
        return put(Endpoints.PRODUCT_BY_ID, product, Collections.emptyMap());
    }
    
    /**
     * Delete product
     * @param productId product ID
     * @return Response with deletion status
     */
    public Response deleteProduct(int productId) {
        LoggerUtil.info("Deleting product with ID: {}", productId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", productId);
        return delete(Endpoints.PRODUCT_BY_ID, null);
    }
    
    /**
     * Search products by name
     * @param name product name to search
     * @return Response with matching products
     */
    public Response searchProductsByName(String name) {
        LoggerUtil.info("Searching products with name: {}", name);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", name);
        return getWithQueryParams(Endpoints.PRODUCTS, queryParams, null);
    }
}