package edu.java.bot.processor.commands;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@ExtendWith(MockitoExtension.class)
public class HelpCommandTest extends CommandTest {
    private Command help;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        List<Command> commandList = new ArrayList<>();
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
