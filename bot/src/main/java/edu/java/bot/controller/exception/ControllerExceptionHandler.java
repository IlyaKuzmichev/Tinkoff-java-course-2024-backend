package edu.java.bot.controller.exception;

import edu.java.bot.controller.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.bot.controller"})
public class ControllerExceptionHandler {
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
            HttpStatus.NOT_FOUND.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
