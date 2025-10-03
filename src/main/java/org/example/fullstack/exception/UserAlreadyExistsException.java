package org.example.fullstack.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
