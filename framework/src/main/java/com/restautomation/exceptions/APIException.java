package com.restautomation.exceptions;

/**
 * APIException - Custom exception for API errors
 */
public class APIException extends RuntimeException {
    private final int statusCode;
    
    /**
     * Constructor with message
     * @param message error message
     */
    public APIException(String message) {
        super(message);
        this.statusCode = 0;
    }
    
    /**
     * Constructor with message and status code
     * @param message error message
     * @param statusCode HTTP status code
     */
    public APIException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    /**
     * Constructor with message and cause
     * @param message error message
     * @param cause exception cause
     */
    public APIException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }
    
    /**
     * Constructor with message, status code and cause
     * @param message error message
     * @param statusCode HTTP status code
     * @param cause exception cause
     */
    public APIException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    /**
     * Get HTTP status code
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}