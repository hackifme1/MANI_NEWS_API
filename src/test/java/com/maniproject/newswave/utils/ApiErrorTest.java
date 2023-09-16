package com.maniproject.newswave.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiErrorTest {

    @Test
    public void testApiErrorCreation() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        int statusCode = status.value();
        String message = "Bad request";

        ApiError apiError = new ApiError(status, statusCode, message);

        assertEquals(status, apiError.getStatus());
        assertEquals(statusCode, apiError.getStatusCode());
        assertEquals(message, apiError.getMessage());
    }
}
