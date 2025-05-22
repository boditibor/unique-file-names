package com.tiborbodi.uniquefilenames.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleFileOperationException_shouldReturnInternalServerError() {
        FileOperationException ex = new FileOperationException("File error");
        ResponseEntity<String> response = handler.handleFileOperationException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File error", response.getBody());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception ex = new Exception("Some error");
        ResponseEntity<String> response = handler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}

