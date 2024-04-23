package edu.java.service.update_sender;

import edu.java.clients.bot.dto.LinkUpdateRequest;

public interface LinkUpdateSenderService {
    void sendUpdate(LinkUpdateRequest updateRequest);
}
