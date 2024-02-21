package edu.java.bot.processor.commands;

import edu.java.bot.database.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrackCommandTest extends CommandTest {
    private Command track;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        track = new TrackCommand(userRegistry);
    }

    @Test
    public void testTryTrackWithRegistration() {
        userRegistry.putUser(new User(chatId));
        var returnMessage = track.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "Input the link for tracking");
    }

    @Test
    public void testTryTrackWithoutRegistration() {
        var returnMessage = track.execute(update);
        assertFalse(returnMessage.isEmpty());
        assertEquals(returnMessage, "You need to be registered for tracking links");
    }
}
