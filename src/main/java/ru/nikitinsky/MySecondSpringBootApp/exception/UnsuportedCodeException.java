package ru.nikitinsky.MySecondSpringBootApp.exception;

public class UnsuportedCodeException extends Exception {
    public UnsuportedCodeException(String message) {
        super(message);
    }
}
