package edu.java.bot.processor.commands;

import edu.java.bot.database.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest extends CommandTest{
    private Command list;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        list = new ListCommand(userRegistry);
    }

    @BeforeEach
    public void mocMessageText() {
        Mockito.doReturn("/list").when(message).text();
    }

    @Test
    public void testEmptyListOfUserLinks() {
        User user = new User(chatId);
        userRegistry.putUser(user);

        var returnMessage = list.handle(update);
        assertTrue(returnMessage.isPresent());
        assertEquals(returnMessage.get(), "You have no links for tracking");
    }

    @Test
    public void testUnregisteredUserTryToGetLinks() {
        var returnMessage = list.handle(update);
        assertTrue(returnMessage.isPresent());
        assertEquals(returnMessage.get(), "Need to be registered for tracking links");
    }

    @Test
    public void testOneLinkInUsersList() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        userRegistry.addLink(user, "abc", "abc");

        var returnMessage = list.handle(update);
        assertTrue(returnMessage.isPresent());
        assertTrue(returnMessage.get().startsWith("Your tracking links:"));
        assertTrue(returnMessage.get().endsWith("abc\n"));
    }

    @Test
    public void testSeveralLinksInUsersList() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        userRegistry.addLink(user, "A", "A");
        userRegistry.addLink(user, "B", "B");
        userRegistry.addLink(user, "C", "C");

        var returnMessage = list.handle(update);
        assertTrue(returnMessage.isPresent());
        assertTrue(returnMessage.get().startsWith("Your tracking"));
        assertTrue(returnMessage.get().endsWith("2. B\n3. C\n"));
    }
}
