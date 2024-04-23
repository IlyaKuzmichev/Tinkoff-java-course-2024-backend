package edu.java.clients.bot;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import reactor.core.publisher.Mono;

public interface BotClient {
    Mono<Void> sendUpdates(LinkUpdateRequest updateRequest);
}
