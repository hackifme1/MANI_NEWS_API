package com.maniproject.newswave.utils;

import org.springframework.http.HttpStatus;

public class ApiError {
    private HttpStatus status;
    private int statusCode;
    private String message;

    public ApiError(HttpStatus status, int statusCode, String message) {
        this.status = status;
        this.message = message;
        this.statusCode= statusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
    public  int getStatusCode(){
        return statusCode;
    }
    public String getMessage() {
        return message;
    }


}
