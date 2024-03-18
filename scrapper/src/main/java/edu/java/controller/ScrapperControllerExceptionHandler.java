package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UserIdNotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.controller"})
public class ScrapperControllerExceptionHandler {
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

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatIdNotFoundException(
        UserIdNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Chat id for user not found",
            String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotFoundException(
        LinkNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Link not found in user's list",
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AttemptDoubleRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> handleAttemptDoubleRegistrationException(
        AttemptDoubleRegistrationException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Registered user tries to do it one more time",
            String.valueOf(HttpStatus.CONFLICT.value()),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AttemptAddLinkOneMoreTimeException.class)
    public ResponseEntity<ApiErrorResponse> handleAttemptAddLinkOneMoreTimeException(
        AttemptAddLinkOneMoreTimeException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Trying to add link, that is already tracking",
            String.valueOf(HttpStatus.I_AM_A_TEAPOT.value()),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.asList(Arrays.toString(ex.getStackTrace()).split(", "))
        );
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }
}

