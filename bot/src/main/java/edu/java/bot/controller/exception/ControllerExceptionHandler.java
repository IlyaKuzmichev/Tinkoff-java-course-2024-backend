package edu.java.bot.controller.exception;

import edu.java.bot.controller.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.bot.controller"})
public class ControllerExceptionHandler {
    @ExceptionHandler(IncorrectRequestParametersException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectRequestParametersException(
        IncorrectRequestParametersException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Incorrect request parameters",
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ChatIdNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(ChatIdNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Chat id for user not found",
            String.valueOf(HttpStatus.NOT_FOUND),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
