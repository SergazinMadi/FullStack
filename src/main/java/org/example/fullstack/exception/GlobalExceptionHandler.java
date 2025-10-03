package org.example.fullstack.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String, Object> buildResponse(HttpStatus status, String error, String message, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        response.put("path", request.getDescription(false));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserExists(UserAlreadyExistsException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.CONFLICT, "User Already Exists", ex.getMessage(), request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.NOT_FOUND, "Entity Not Found", ex.getMessage(), request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", String.join(", ", errors), request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParamsException(MissingServletRequestParameterException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, "Missing Request Parameter", ex.getParameterName() + " parameter is missing", request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, "Illegal Argument", ex.getMessage(), request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.NOT_FOUND, "Username Not Found", ex.getMessage(), request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, "Illegal State", ex.getMessage(), request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionException(TransactionSystemException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("path", request.getDescription(false));
        body.put("error", "Internal Server Error");
        body.put("message", "Transaction failed: " + ex.getMostSpecificCause().getMessage());
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}