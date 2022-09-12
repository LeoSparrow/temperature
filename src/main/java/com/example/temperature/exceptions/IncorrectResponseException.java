package com.example.temperature.exceptions;

public class IncorrectResponseException extends RuntimeException {
    public IncorrectResponseException(String message) {
        super(message);
    }
}
