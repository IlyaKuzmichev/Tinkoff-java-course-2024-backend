package edu.java.bot.processor.commands;

import edu.java.bot.database.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrackCommandTest extends CommandTest {
    private Command track;

    @Override
    public void mocTestPreprocessor() {
        super.mocTestPreprocessor();
        track = new TrackCommand(userRegistry);
    }

    @BeforeEach
    public void mocMessageText() {
        Mockito.doReturn("/track").when(message).text();
    }

    @Test
    public void testTryTrackWithRegistration() {
        userRegistry.putUser(new User(chatId));
        var returnMessage = track.handle(update);
        assertTrue(returnMessage.isPresent());
        assertEquals(returnMessage.get(), "Input the link for tracking");
    }

    @Test
    public void testTryTrackWithoutRegistration() {
        var returnMessage = track.handle(update);
        assertTrue(returnMessage.isPresent());
        assertEquals(returnMessage.get(), "You need to be registered for tracking links");
    }
}
