package edu.java.bot.command;

import edu.java.bot.database.UserRegistry;
import org.apache.catalina.User;
import java.util.ArrayList;
import java.util.Optional;

public class CommandHandler {
    private final ArrayList<Command> commandList = new ArrayList<>();

    public CommandHandler(UserRegistry userRegistry) {
        commandList.add(new StartCommand(userRegistry));
        commandList.add(new TrackCommand(userRegistry));
        commandList.add(new UntrackCommand(userRegistry));
        commandList.add(new ListCommand(userRegistry));
        commandList.add(new HelpCommand(commandList));

        for (var i = 0; i < commandList.size() - 1; ++i) {
            commandList.get(i).setNextCommand(commandList.get(i + 1));
        }
    }

    public Command chainStart() {
        return commandList.get(0);
    }

}
