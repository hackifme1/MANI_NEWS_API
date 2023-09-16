package com.maniproject.newswave.exception;

public class UserNotVerified extends RuntimeException{
    public UserNotVerified(String message) {
        super(message);
    }
}
