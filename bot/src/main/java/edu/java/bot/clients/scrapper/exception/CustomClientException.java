package edu.java.bot.clients.scrapper.exception;

import edu.java.bot.clients.scrapper.dto.ClientErrorResponse;
import lombok.Getter;

@Getter
public class CustomClientException extends RuntimeException {
    private final ClientErrorResponse clientErrorResponse;

    public CustomClientException(ClientErrorResponse clientErrorResponse) {
        this.clientErrorResponse = clientErrorResponse;
    }
}
