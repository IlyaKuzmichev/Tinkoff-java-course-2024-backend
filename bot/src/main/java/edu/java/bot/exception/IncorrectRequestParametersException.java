package edu.java.bot.exception;

/**
 * Exception class for API (Incorrect request parameters)
 */
public class IncorrectRequestParametersException extends RuntimeException {
    public IncorrectRequestParametersException(String errorMessage) {
        super(errorMessage);
    }
}
