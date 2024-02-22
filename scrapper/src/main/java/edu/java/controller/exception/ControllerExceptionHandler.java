package edu.java.controller.exception;

import edu.java.controller.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.controller"})
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
            HttpStatus.NOT_ACCEPTABLE.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotFoundException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Link not found in user's list",
            HttpStatus.NOT_FOUND.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AttemptDoubleRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> handleAttemptDoubleRegistrationException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Registered user tries to do it one more time",
            HttpStatus.CONFLICT.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AttemptAddLinkOneMoreTimeException.class)
    public ResponseEntity<ApiErrorResponse> handleAttemptAddLinkOneMoreTimeException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            "Trying to add link, that is already tracking",
            HttpStatus.I_AM_A_TEAPOT.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            null // stacktrace
        );
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }
}

