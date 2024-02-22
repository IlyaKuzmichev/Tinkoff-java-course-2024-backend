package edu.java.bot.controller.dto;

import java.util.List;

public record LinkUpdateRequest(Long id, String url, String description, List<Long> tgChatIds) {
}


