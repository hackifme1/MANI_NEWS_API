package com.maniproject.newswave.exception;

public class InvalidCategoryCode extends RuntimeException{
    public InvalidCategoryCode(String message) {
        super(message);
    }
}
