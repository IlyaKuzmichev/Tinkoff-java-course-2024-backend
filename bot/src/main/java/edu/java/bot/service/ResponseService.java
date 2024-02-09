package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandHandler;
import edu.java.bot.database.UserRegistry;

public class ResponseService {
    private final CommandHandler commandHandler;

    public ResponseService() {
        UserRegistry userRegistry = new UserRegistry();
        commandHandler = new CommandHandler(userRegistry);
    }
    public SendMessage getAnswer(Update update) {
        var result = commandHandler.chainStart().handle(update);
        return result.map(s -> new SendMessage(update.message().chat().id(), s))
            .orElseGet(() -> nonCommandHandler(update));
    }

    public SendMessage nonCommandHandler(Update update) {
        return new SendMessage(update.message().chat().id(), "Abobius");
    }
}
