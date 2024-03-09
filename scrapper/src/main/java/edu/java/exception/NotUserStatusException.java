package edu.java.exception;

public class NotUserStatusException extends RuntimeException {
    public NotUserStatusException(String pseudostatus) {
        super(pseudostatus + " does not belong to User.Status values");
    }
}
