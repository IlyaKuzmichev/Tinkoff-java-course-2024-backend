package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.dto.UserStatus;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/track")
@Qualifier("command_list_add")
public final class TrackCommand implements Command {
    private static final String NAME = "/track";
    private static final String DESCRIPTION = "Add new link for tracking";
    private static final String LINK_INVITATION = "Input the link for tracking";
    ScrapperClient scrapperClient;

    public TrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String commandName() {
        return NAME;
    }

    @Override
    public String commandDescription() {
        return DESCRIPTION;
    }

    @Override
    public String execute(Update update) {
        var chatId = update.message().chat().id();
        try {
            scrapperClient.setUserStatus(chatId, UserStatus.TRACK_LINK).block();
        } catch (CustomClientException e) {
            return e.getClientErrorResponse().exceptionMessage();
        }
        return LINK_INVITATION;
    }
}

