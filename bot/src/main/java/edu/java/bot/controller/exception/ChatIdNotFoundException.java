package edu.java.bot.controller.exception;

public class ChatIdNotFoundException extends RuntimeException {
    public ChatIdNotFoundException(String message) {
        super(message);
    }
}
