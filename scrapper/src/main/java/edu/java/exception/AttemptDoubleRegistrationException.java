package edu.java.exception;

public class AttemptDoubleRegistrationException extends RuntimeException {
    private final Long userId;
    public AttemptDoubleRegistrationException(Long userId) {
        super("Already registered");
        this.userId = userId;
    }
}
