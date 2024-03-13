package edu.java.clients.bot.dto;

import java.util.List;

public record ClientErrorResponse(String description, String code, String exceptionName,
                                  String exceptionMessage, List<String> stacktrace) {
}
