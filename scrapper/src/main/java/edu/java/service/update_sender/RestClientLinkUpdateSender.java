package edu.java.service.update_sender;

import edu.java.clients.bot.BotClient;
import edu.java.clients.bot.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestClientLinkUpdateSender implements LinkUpdateSenderService {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdateRequest updateRequest) {
        botClient.sendUpdates(updateRequest).block();
    }
}
