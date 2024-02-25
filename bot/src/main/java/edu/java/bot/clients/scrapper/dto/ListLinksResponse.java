package edu.java.bot.clients.scrapper.dto;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}
