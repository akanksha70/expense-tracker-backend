package com.expensetracker.backend.exception;

public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedAccessException(String resourceName, Long resourceId, Long userId) {
        super(String.format("User %d is not authorized to access %s with id %d", userId, resourceName, resourceId));
    }
}