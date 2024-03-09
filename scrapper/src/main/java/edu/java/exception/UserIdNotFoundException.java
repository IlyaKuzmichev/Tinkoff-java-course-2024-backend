package edu.java.exception;

import lombok.Getter;

@Getter
public class UserIdNotFoundException extends RuntimeException {
    private final Long userId;

    public UserIdNotFoundException(Long userId) {
        this.userId = userId;
    }
}
