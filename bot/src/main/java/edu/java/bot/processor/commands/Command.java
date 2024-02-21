package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

public interface Command {
    String commandName();

    String commandDescription();

    String execute(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(commandName(), commandDescription());
    }
}
