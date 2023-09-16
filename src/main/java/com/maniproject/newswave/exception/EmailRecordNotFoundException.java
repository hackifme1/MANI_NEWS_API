package com.maniproject.newswave.exception;

public class EmailRecordNotFoundException extends RuntimeException{
    public EmailRecordNotFoundException(String message) {
        super(message);
    }
}
