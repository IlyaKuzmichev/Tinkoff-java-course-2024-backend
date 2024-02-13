package edu.java.bot.processor.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.database.UserRegistry;
import edu.java.bot.processor.commands.CommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerTest {
    private CommandHandler commandHandler;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    Long chatId;

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

    @Test
    public void testChainOfResponsibilityHandler() {
        Mockito.doReturn("/list").when(message).text();
        chatId = 42L;
        Mockito.doReturn(message).when(update).message();
        Mockito.doReturn(chat).when(message).chat();
        Mockito.doReturn(chatId).when(chat).id();
        var result = commandHandler.handle(update);
        assertTrue(result.isPresent());
        assertEquals("Need to be registered for tracking links", result.get());
    }
}
