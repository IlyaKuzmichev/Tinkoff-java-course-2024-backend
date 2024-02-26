package edu.java.clients.exception;

import edu.java.clients.bot.dto.ClientErrorResponse;
import lombok.Getter;

@Getter
public class CustomClientException extends RuntimeException {
    private final ClientErrorResponse clientErrorResponse;

    public CustomClientException(ClientErrorResponse clientErrorResponse) {
        this.clientErrorResponse = clientErrorResponse;
    }
}
