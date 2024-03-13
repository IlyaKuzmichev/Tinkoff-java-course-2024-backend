package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.scrapper.ScrapperClient;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/start")
@Qualifier("command_list_add")
public final class StartCommand implements Command {
    private static final String NAME = "/start";
    private static final String DESCRIPTION = "Register new user";
    private static final String REGISTRATION_SUCCESS = "You were successfully registered";
    private static final String ALREADY_REGISTERED = "You're already registered";

    ScrapperClient scrapperClient;

    public StartCommand(ScrapperClient scrapperClient) {
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
            scrapperClient.registerChat(chatId).block();
        } catch (CustomClientException e) {
            return e.getClientErrorResponse().exceptionMessage();
        }
        return REGISTRATION_SUCCESS;
    }
}
