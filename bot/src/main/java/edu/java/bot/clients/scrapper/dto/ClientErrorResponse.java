package edu.java.bot.clients.scrapper.dto;

import java.util.List;

public record ClientErrorResponse(String description, String code, String exceptionName,
                                  String exceptionMessage, List<String> stacktrace) {
}
