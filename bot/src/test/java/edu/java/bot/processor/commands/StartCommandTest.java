package edu.java.bot.processor.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartCommandTest extends CommandTest {
    private Command start;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        start = new StartCommand(userRegistry);
    }

    @BeforeEach
    public void mocMessageText() {
        Mockito.doReturn("/start").when(message).text();
    }

    @Test
    public void testNewUserAddedToRegistry() {
        var returnMessage = start.handle(update);
        assertTrue(returnMessage.isPresent());

        var user = userRegistry.getUser(chatId);
        assertTrue(user.isPresent());

        assertEquals(returnMessage.get(), "You were successfully registered");
        assertEquals(user.get().getLinks().size(), 0);
    }

    @Test
    public void testUserAlreadyRegistered() {
        start.handle(update);
        userRegistry.addLink(userRegistry.getUser(chatId).get(), "oleg", "privet");
        var returnMessage = start.handle(update);
        assertTrue(returnMessage.isPresent());
        assertEquals(returnMessage.get(), "You're already registered");

        var user = userRegistry.getUser(chatId);
        assertTrue(user.isPresent());
        assertEquals(userRegistry.getUser(chatId).get().getLinks().size(), 1);
    }
}
