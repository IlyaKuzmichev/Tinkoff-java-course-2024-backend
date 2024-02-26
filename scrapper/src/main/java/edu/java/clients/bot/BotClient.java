package edu.java.clients.bot;

import java.util.List;
import reactor.core.publisher.Mono;

public interface BotClient {
    Mono<Void> sendUpdates(Long id, String url, String description, List<Long> tgChatIds);
}
