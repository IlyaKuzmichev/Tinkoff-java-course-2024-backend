package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/help")
public final class HelpCommand implements Command {
    private static final String NAME = "/help";
    private static final String DESCRIPTION = "Print instructions for the bot";
    private static final String FIRST_STRING = "Allowed commands for bot:\n";
    private static final String FORMATTED_STRING = "%s - %s\n";
    private final List<Command> commandList;

    public HelpCommand(@Qualifier("command_list_add") List<Command> commandList) {
        this.commandList = commandList;
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
        StringBuilder builder = new StringBuilder();
        builder.append(FIRST_STRING);
        for (Command command : commandList) {
            builder.append(FORMATTED_STRING.formatted(command.commandName(), command.commandDescription()));
        }
        return builder.toString();
    }
}
