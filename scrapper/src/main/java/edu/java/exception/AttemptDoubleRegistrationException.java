package edu.java.exception;

public class AttemptDoubleRegistrationException extends RuntimeException {
    public AttemptDoubleRegistrationException(String message) {
        super(message);
    }
}
