package edu.java.bot.processor.commands;

import edu.java.bot.database.UserRegistry;
import edu.java.bot.processor.commands.CommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandHandlerTest {
    private CommandHandler commandHandler;

    @BeforeEach
    public void initHandler() {
        commandHandler = new CommandHandler(new UserRegistry());
    }

    @Test
    public void testCommandHandlerStorage() {
        assertNotNull(commandHandler);

        var commandList = commandHandler.getCommandList();
        assertEquals(commandList.size(), 5);
        assertEquals(commandList.get(0).command(), "/start");
        assertEquals(commandList.get(1).command(), "/track");
        assertEquals(commandList.get(2).command(), "/untrack");
        assertEquals(commandList.get(3).command(), "/list");
        assertEquals(commandList.get(4).command(), "/help");

        assertEquals(commandList.get(0).description(), "Command to start the bot");
        assertEquals(commandList.get(1).description(), "Add new link for tracking");
        assertEquals(commandList.get(2).description(), "Untrack one of your links");
        assertEquals(commandList.get(3).description(), "Shows list of tracking links");
        assertEquals(commandList.get(4).description(), "Print instructions for the bot");
    }
}
