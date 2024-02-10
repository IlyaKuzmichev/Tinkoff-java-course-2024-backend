package edu.java.bot.processor.commands;

import edu.java.bot.database.UserRegistry;
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;



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

    public Command[] getCommandList() {
        Command[] array = new Command[commandList.size()];
        for (var i = 0; i < commandList.size(); ++i) {
            array[i] = commandList.get(i);
        }
        return array;
    }
    public Command chainStart() {
        return commandList.get(0);
    }

}
