package org.example.fullstack.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
    
    public InvalidStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
