package edu.java.bot.processor.commands;

import edu.java.bot.database.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest extends CommandTest{
    private Command list;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        list = new ListCommand(userRegistry);
    }

    @Test
    public void testEmptyListOfUserLinks() {
        User user = new User(chatId);
        userRegistry.putUser(user);

        var returnMessage = list.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "You have no links for tracking");
    }

    @Test
    public void testUnregisteredUserTryToGetLinks() {
        var returnMessage = list.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "Need to be registered for tracking links");
    }

    @Test
    public void testOneLinkInUsersList() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        userRegistry.addLink(user, "abc", "abc");

        var returnMessage = list.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertTrue(returnMessage.startsWith("Your tracking links:"));
        assertTrue(returnMessage.endsWith("abc\n"));
    }

    @Test
    public void testSeveralLinksInUsersList() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        userRegistry.addLink(user, "A", "A");
        userRegistry.addLink(user, "B", "B");
        userRegistry.addLink(user, "C", "C");

        var returnMessage = list.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertTrue(returnMessage.startsWith("Your tracking"));
        assertTrue(returnMessage.endsWith("2. B\n3. C\n"));
    }
}
