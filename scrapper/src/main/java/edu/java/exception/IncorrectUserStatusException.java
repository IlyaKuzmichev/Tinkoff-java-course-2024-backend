package edu.java.exception;

import lombok.Getter;

@Getter
public class IncorrectUserStatusException extends RuntimeException {
    private final Long userId;

    public IncorrectUserStatusException(Long userId) {
        this.userId = userId;
    }
}
