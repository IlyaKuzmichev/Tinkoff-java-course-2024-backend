package edu.java.bot.exception;

import edu.java.bot.controller.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IncorrectRequestParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectRequestParametersException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Incorrect request parameters",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ChatIdNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Chat id for user not found",
            HttpStatus.NOT_ACCEPTABLE.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }
}
