package com.maniproject.newswave.exception;

public class InvalidNewsProvider extends RuntimeException {
    public InvalidNewsProvider(String message) {
        super(message);
    }
}
