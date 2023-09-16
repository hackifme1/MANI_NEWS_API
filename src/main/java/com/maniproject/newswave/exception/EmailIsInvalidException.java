package com.maniproject.newswave.exception;

public class EmailIsInvalidException extends RuntimeException{
    public EmailIsInvalidException(String message) {
        super(message);
    }
}
