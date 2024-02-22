package edu.java.controller.exception;

public class IncorrectRequestParametersException extends RuntimeException {
    public IncorrectRequestParametersException(String errorMessage) {
        super(errorMessage);
    }
}
