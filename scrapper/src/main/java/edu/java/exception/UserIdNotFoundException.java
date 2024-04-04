package edu.java.exception;

import lombok.Getter;

@Getter
public class UserIdNotFoundException extends RuntimeException {
    private final Long userId;

    public UserIdNotFoundException(Long userId) {
        super("Registration required");
        this.userId = userId;
    }
}
