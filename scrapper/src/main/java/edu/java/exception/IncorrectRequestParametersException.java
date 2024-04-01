package edu.java.exception;

public class IncorrectRequestParametersException extends RuntimeException {
    public IncorrectRequestParametersException(String errorMessage) {
        super(errorMessage);
    }
}
