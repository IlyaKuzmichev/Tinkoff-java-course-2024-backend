package edu.java.bot.database;

import org.junit.jupiter.api.Test;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserTest {
    @Test
    public void testUserClass() {
        Long id = 42L;
        UserState state = UserState.BASE;
        User user = new User(id);

        assertEquals(id, user.getId());
        assertEquals(state, user.getState());
        assertEquals(0, user.getLinks().size());
        assertFalse(user.isTracking("aboba"));
        user.setState(UserState.WAIT_TRACK_URI);
        assertEquals(UserState.WAIT_TRACK_URI, user.getState());


    }
}
