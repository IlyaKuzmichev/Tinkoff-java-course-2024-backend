package edu.java.exception;

public class CorruptedDataException extends RuntimeException {
    public CorruptedDataException(String message) {
        super(message);
    }
}
