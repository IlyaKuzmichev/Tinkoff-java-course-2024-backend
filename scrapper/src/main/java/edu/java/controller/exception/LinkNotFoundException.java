package edu.java.controller.exception;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String message) {
        super(message);
    }
}
