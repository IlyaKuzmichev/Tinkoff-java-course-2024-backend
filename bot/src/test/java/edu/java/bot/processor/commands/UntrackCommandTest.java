package edu.java.bot.processor.commands;

import edu.java.bot.database.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UntrackCommandTest extends CommandTest {
    private Command untrack;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        untrack = new UntrackCommand(userRegistry);
    }

    @Test
    public void testTryUntrackWithRegistration() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        userRegistry.addLink(user, "a", "v");
        var returnMessage = untrack.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "Input link for untracking");
    }

    @Test
    public void testTryUntrackWithEmptyLinksList() {
        User user = new User(chatId);
        userRegistry.putUser(user);
        var returnMessage = untrack.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "You have no tracking links");
    }

    @Test
    public void testTryTrackWithoutRegistration() {
        var returnMessage = untrack.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "You need to be registered for tracking links");
    }
}
