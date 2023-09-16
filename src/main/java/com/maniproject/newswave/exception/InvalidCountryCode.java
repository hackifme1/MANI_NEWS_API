package com.maniproject.newswave.exception;

public class InvalidCountryCode extends RuntimeException{
    public InvalidCountryCode(String message) {
        super(message);
    }
}
