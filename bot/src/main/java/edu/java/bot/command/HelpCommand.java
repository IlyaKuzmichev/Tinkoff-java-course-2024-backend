package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import java.util.ArrayList;
import java.util.Optional;

final class HelpCommand extends Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "Print instructions for the bot";
    private static final String FIRST_STRING = "Allowed commands for bot:\n";
    private static final String FORMATTED_STRING = "%s - %s\n";
    ArrayList<Command> commandList;

    public HelpCommand(ArrayList<Command> commandList) {
        super();
        this.commandList = commandList;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    protected Optional<String> execute(Update update) {
        StringBuilder builder = new StringBuilder();
        builder.append(FIRST_STRING);
        for (Command command : commandList) {
            builder.append(FORMATTED_STRING.formatted(command.command(), command.description()));
        }
        return Optional.of(builder.toString());
    }
}
