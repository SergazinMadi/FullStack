package org.example.fullstack.exception;

public class PickupPointNotFoundException extends RuntimeException {
    public PickupPointNotFoundException(String message) {
        super(message);
    }
    
    public PickupPointNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
