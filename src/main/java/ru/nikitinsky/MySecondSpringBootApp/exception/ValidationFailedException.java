package ru.nikitinsky.MySecondSpringBootApp.exception;

public class ValidationFailedException extends Exception {
    public ValidationFailedException(String message) {
        super(message);
    }
}
