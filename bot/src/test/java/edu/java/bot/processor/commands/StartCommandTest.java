package edu.java.bot.processor.commands;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class StartCommandTest extends CommandTest {
//    private Command start;
//
//    @Override
//    public void mocTestPreprocessor() {
//        super.mocTestPreprocessor();
//        start = new StartCommand(scrapperClient);
//    }
//
//    @Test
//    public void testNewUserAddedToRegistry() {
//        var returnMessage = start.execute(update);
//        assertFalse(returnMessage.isEmpty());
//
//        var user = userRegistry.getUser(chatId);
//        assertTrue(user.isPresent());
//
//        assertEquals(returnMessage, "You were successfully registered");
//        assertEquals(user.get().getLinks().size(), 0);
//    }
//
//    @Test
//    public void testUserAlreadyRegistered() {
//        start.execute(update);
//        userRegistry.addLink(userRegistry.getUser(chatId).get(), "oleg", "privet");
//        var returnMessage = start.execute(update);
//        assertFalse(returnMessage.isEmpty());
//        assertEquals(returnMessage, "You're already registered");
//
//        var user = userRegistry.getUser(chatId);
//        assertTrue(user.isPresent());
//        assertEquals(userRegistry.getUser(chatId).get().getLinks().size(), 1);
//    }
}
