package edu.java.clients.bot;

import java.net.URI;
import java.util.List;
import reactor.core.publisher.Mono;

public interface BotClient {
    Mono<Void> sendUpdates(Long id, URI url, String description, List<Long> tgChatIds);
}
