package com.restautomation.api;

import com.restautomation.base.BaseAPI;
import com.restautomation.constants.Endpoints;
import com.restautomation.models.Todo;
import com.restautomation.utils.LoggerUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * TodoAPI - API methods for todo endpoints
 */
public class TodoAPI extends BaseAPI {
    
    /**
     * Get all todos
     * @return Response with all todos
     */
    public Response getAllTodos() {
        LoggerUtil.info("Getting all todos");
        return get(Endpoints.TODOS, null);
    }
    
    /**
     * Get todo by ID
     * @param todoId todo ID
     * @return Response with todo details
     */
    public Response getTodoById(int todoId) {
        LoggerUtil.info("Getting todo with ID: {}", todoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", todoId);
        return get(Endpoints.TODO_BY_ID, pathParams, null);
    }
    
    /**
     * Create new todo
     * @param todo Todo object
     * @return Response with created todo
     */
    public Response createTodo(Todo todo) {
        LoggerUtil.info("Creating new todo: {}", todo);
        return post(Endpoints.TODOS, todo, null);
    }
    
    /**
     * Update todo
     * @param todoId todo ID
     * @param todo Updated Todo object
     * @return Response with updated todo
     */
    public Response updateTodo(int todoId, Todo todo) {
        LoggerUtil.info("Updating todo with ID: {}", todoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", todoId);
        return put(Endpoints.TODO_BY_ID, todo, pathParams, null);
    }
    
    /**
     * Delete todo
     * @param todoId todo ID
     * @return Response with deletion status
     */
    public Response deleteTodo(int todoId) {
        LoggerUtil.info("Deleting todo with ID: {}", todoId);
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", todoId);
        return delete(Endpoints.TODO_BY_ID, pathParams, null);
    }
} 