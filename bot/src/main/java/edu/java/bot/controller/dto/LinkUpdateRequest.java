package edu.java.bot.controller.dto;

import java.util.List;


public record LinkUpdateRequest(Long id, String url, String description, List<Long> tgChatIds) {
    @Override public String toString() {
        return "LinkUpdateRequest{"
            + "id=" + id
            + ", url='" + url + '\''
            + ", description='" + description + '\''
            + ", tgChatIds=" + tgChatIds
            + '}';
    }
}


