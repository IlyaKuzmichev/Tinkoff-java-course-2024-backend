package edu.java.bot.database;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistryTest {

    private static final String LINK_GITHUB = "https://github.com";
    private static final String LINK_STACK_OVERFLOW = "https://stackoverflow.com";

    @Test
    public void testUserRegistryStorage() {
        UserRegistry registry = new UserRegistry();

        User user1 = new User(1L);
        User user2 = new User(2L);
        User user3 = new User(3L);
        registry.putUser(user1);
        registry.putUser(user2);
        registry.putUser(user3);

        assertEquals(registry.getUser(1L), Optional.of(user1));
        assertEquals(registry.getUser(2L), Optional.of(user2));
        assertEquals(registry.getUser(3L), Optional.of(user3));
        assertEquals(registry.getUser(4L), Optional.empty());

        registry.addLink(user1, LINK_GITHUB, LINK_GITHUB);

        assertThat(registry.getUser(1L).get().getLinks().size()).isEqualTo(1);
        assertTrue(user1.getLinks().contains(LINK_GITHUB));
        assertFalse(user1.getLinks().contains(LINK_STACK_OVERFLOW));

        registry.addLink(user2, LINK_STACK_OVERFLOW, LINK_STACK_OVERFLOW);
        registry.addLink(user2, LINK_STACK_OVERFLOW, LINK_STACK_OVERFLOW);
        registry.addLink(user2, LINK_STACK_OVERFLOW, LINK_STACK_OVERFLOW);

        assertThat(registry.getUser(2L).get().getLinks().size()).isEqualTo(1);
        assertTrue(user2.getLinks().contains(LINK_STACK_OVERFLOW));
        assertFalse(user2.getLinks().contains(LINK_GITHUB));

        registry.addLink(user3, LINK_STACK_OVERFLOW, LINK_STACK_OVERFLOW);
        registry.addLink(user3, LINK_GITHUB, LINK_GITHUB);
        assertThat(user3.getLinks().size()).isEqualTo(2);

        registry.removeLink(user3, LINK_STACK_OVERFLOW, LINK_STACK_OVERFLOW);
        assertTrue(user3.getLinks().contains(LINK_GITHUB));
        assertFalse(user3.getLinks().contains(LINK_STACK_OVERFLOW));
        assertEquals(user3.getLinks().size(), 1);
    }
}
