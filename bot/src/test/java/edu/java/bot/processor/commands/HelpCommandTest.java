package edu.java.bot.processor.commands;

import edu.java.bot.database.UserRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest extends CommandTest {
    private Command help;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        List<Command> commandList = new ArrayList<>();
        commandList.add(new StartCommand(new UserRegistry()));
        help = new HelpCommand(commandList);
    }

    @Test
    public void testEasyForHelpCommand() {
        var user = update.message().chat().id();
        assertEquals(user, 1984L);
        var returnMessage = help.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertTrue(returnMessage.startsWith("Allowed commands for bot:"));
        assertTrue(returnMessage.endsWith("Register new user\n"));
    }
}
