package edu.java.controller.exception;

public class AttemptDoubleRegistrationException extends RuntimeException {
    public AttemptDoubleRegistrationException(String message) {
        super(message);
    }
}
