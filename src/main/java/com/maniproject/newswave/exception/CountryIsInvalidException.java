package com.maniproject.newswave.exception;

public class CountryIsInvalidException extends RuntimeException{
    public CountryIsInvalidException(String message) {
        super(message);
    }
}
