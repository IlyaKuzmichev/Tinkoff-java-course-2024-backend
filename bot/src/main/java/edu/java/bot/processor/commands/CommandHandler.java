package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import java.util.ArrayList;
import java.util.Optional;

public class CommandHandler {
    private final ArrayList<Command> commandList;

    public CommandHandler(UserRegistry userRegistry) {
        commandList = new ArrayList<>();
        commandList.add(new StartCommand(userRegistry));
        commandList.add(new TrackCommand(userRegistry));
        commandList.add(new UntrackCommand(userRegistry));
        commandList.add(new ListCommand(userRegistry));
        commandList.add(new HelpCommand(commandList));

        for (var i = 0; i < commandList.size() - 1; ++i) {
            commandList.get(i).setNextCommand(commandList.get(i + 1));
        }
    }

    public ArrayList<Command> getCommandList() {
        return commandList;
    }

    public Optional<String> handle(Update update) {
        return commandList.get(0).handle(update);
    }

}
