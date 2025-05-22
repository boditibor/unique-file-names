package com.tiborbodi.uniquefilenames.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Handles generic and file operation exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all generic exceptions.
     * @param ex the exception
     * @return a response entity with error message and status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles file operation exceptions.
     * @param ex the file operation exception
     * @return a response entity with error message and status
     */
    @ExceptionHandler(FileOperationException.class)
    public ResponseEntity<String> handleFileOperationException(FileOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
