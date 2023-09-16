package com.maniproject.newswave.exception;

public class CategoryIsInvalidException extends RuntimeException{
    public CategoryIsInvalidException(String message) {
        super(message);
    }
}
