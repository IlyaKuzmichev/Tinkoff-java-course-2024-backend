package edu.java.bot.processor.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest extends CommandTest {
    private Command help;
    CommandHandler commandHandler;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        commandHandler = new CommandHandler(userRegistry);
        help = new HelpCommand(commandHandler.getCommandList());
    }

    @BeforeEach
    public void mocMessageText() {
        Mockito.doReturn("/help").when(message).text();
    }

    @Test
    public void testEasyForHelpCommand() {
        var returnMessage = help.handle(update);
        assertTrue(returnMessage.isPresent());
        assertTrue(returnMessage.get().startsWith("Allowed commands for bot:"));
        assertTrue(returnMessage.get().endsWith("Print instructions for the bot\n"));
    }
}
