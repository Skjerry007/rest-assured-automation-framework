package com.restautomation.tests;

import com.restautomation.api.TodoAPI;
import com.restautomation.constants.StatusCodes;
import com.restautomation.factory.APIFactory;
import com.restautomation.models.Todo;
import com.restautomation.utils.LoggerUtil;
import com.restautomation.utils.ResponseValidator;
import com.restautomation.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * TodoApiTest - Tests for Todo API endpoints
 */
public class TodoApiTest {
    private TodoAPI todoAPI;
    
    @BeforeClass
    public void setup() {
        // Get TodoAPI instance from factory
        todoAPI = APIFactory.getInstance().getTodoAPI();
    }
    
    @Test(description = "Test getting all todos", retryAnalyzer = RetryAnalyzer.class)
    public void testGetAllTodos() {
        // Get all todos
        Response response = todoAPI.getAllTodos();
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateResponseTime(response, 5000);
        
        // Validate response is a non-empty array
        Assert.assertTrue(response.jsonPath().getList("").size() > 0, "Todos list should not be empty");
        
        LoggerUtil.info("Retrieved {} todos", response.jsonPath().getList("").size());
    }
    
    @Test(description = "Test getting todo by ID", retryAnalyzer = RetryAnalyzer.class)
    public void testGetTodoById() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 201); // 1 to 200 inclusive
        Response response = todoAPI.getTodoById(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "id", randomId);
        ResponseValidator.validateFieldExists(response, "userId");
        ResponseValidator.validateFieldExists(response, "title");
        ResponseValidator.validateFieldExists(response, "completed");
        String todoTitle = response.jsonPath().getString("title");
        LoggerUtil.info("Retrieved todo with title: {}", todoTitle);
    }
    
    @Test(description = "Test creating a new todo", retryAnalyzer = RetryAnalyzer.class)
    public void testCreateTodo() {
        // Create todo object
        Todo todo = Todo.builder()
                .userId(1)
                .title("New Test Todo")
                .completed(false)
                .build();
        
        // Create todo
        Response response = todoAPI.createTodo(todo);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, StatusCodes.CREATED);
        ResponseValidator.validateFieldExists(response, "id");
        ResponseValidator.validateFieldValue(response, "title", "New Test Todo");
        ResponseValidator.validateFieldValue(response, "userId", 1);
        ResponseValidator.validateFieldValue(response, "completed", false);
        
        LoggerUtil.info("Created todo with ID: {}", response.jsonPath().getInt("id"));
    }
    
    @Test(description = "Test updating a todo", retryAnalyzer = RetryAnalyzer.class)
    public void testUpdateTodo() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 201); // 1 to 200 inclusive
        Todo updatedTodo = Todo.builder()
                .userId(1)
                .title("Updated Todo Title")
                .completed(true)
                .build();
        Response response = todoAPI.updateTodo(randomId, updatedTodo);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        ResponseValidator.validateFieldValue(response, "title", "Updated Todo Title");
        ResponseValidator.validateFieldValue(response, "completed", true);
        LoggerUtil.info("Updated todo with ID: {}", randomId);
    }
    
    @Test(description = "Test deleting a todo", retryAnalyzer = RetryAnalyzer.class)
    public void testDeleteTodo() {
        int randomId = ThreadLocalRandom.current().nextInt(1, 201); // 1 to 200 inclusive
        Response response = todoAPI.deleteTodo(randomId);
        ResponseValidator.validateStatusCode(response, StatusCodes.OK);
        LoggerUtil.info("Deleted todo with ID: {}", randomId);
    }
} 