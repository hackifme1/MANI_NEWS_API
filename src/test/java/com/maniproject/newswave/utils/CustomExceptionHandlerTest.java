package com.maniproject.newswave.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExceptionHandlerTest {

    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setup() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    public void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        ResponseEntity<Object> response = customExceptionHandler.handleUserNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleEmailAlreadyExistException() {
        EmailAlreadyExistException exception = new EmailAlreadyExistException("Email already exists");
        ResponseEntity<Object> response = customExceptionHandler.handleEmailAlreadyExistException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    // Add similar test methods for other exception handlers
    @Test
    public void testHandleEmailIsInvalidException() {
        EmailIsInvalidException exception = new EmailIsInvalidException("Email is invalid");
        ResponseEntity<Object> response = customExceptionHandler.handleEmailIsInvalidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleCountryIsInvalidException() {
        CountryIsInvalidException exception = new CountryIsInvalidException("Country is invalid");
        ResponseEntity<Object> response = customExceptionHandler.handleCountryIsInvalidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleCategoryIsInvalidException() {
        CategoryIsInvalidException exception = new CategoryIsInvalidException("Category is invalid");
        ResponseEntity<Object> response = customExceptionHandler.handleCategoryIsInvalidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleEmailRecordNotFoundException() {
        EmailRecordNotFoundException exception = new EmailRecordNotFoundException("Email record not found");
        ResponseEntity<Object> response = customExceptionHandler.handleEmailRecordNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleEmailServiceException() {
        EmailServiceException exception = new EmailServiceException("Email service exception");
        ResponseEntity<Object> response = customExceptionHandler.handleEmailServiceException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument exception");
        ResponseEntity<Object> response = customExceptionHandler.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    @Test
    public void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime exception");
        ResponseEntity<Object> response = customExceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }

    // Add more test methods for other exception handlers

    @Test
    public void testHandleJsonProcessingException() {
        JsonProcessingException exception = new JsonProcessingException("Json processing exception") {};
        ResponseEntity<Object> response = customExceptionHandler.handleJsonProcessingException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ApiError) response.getBody()).getStatusCode());
        assertEquals(exception.getMessage(), ((ApiError) response.getBody()).getMessage());
    }
}
