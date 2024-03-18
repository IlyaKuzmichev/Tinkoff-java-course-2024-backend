package edu.java.domain;

import edu.java.domain.jooq.JooqUserRepository;
import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JooqUserRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqUserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    void testAddUser() {
        User user = new User(123L, User.Status.BASE);
        userRepository.addUser(user);

        List<User> users = userRepository.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveUser() {
        User user = new User(456L, User.Status.BASE);
        userRepository.addUser(user);
        userRepository.removeUser(user.getUserId());

        List<User> users = userRepository.findAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddUserAlreadyExists() {
        User user = new User(5L, null);
        userRepository.addUser(user);

        assertThrows(AttemptDoubleRegistrationException.class, () -> userRepository.addUser(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveNonExistingUser() {
        assertThrows(UserIdNotFoundException.class, () -> userRepository.removeUser(999999999L));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUserByUserId() {
        Long userId = 1984L;
        User user = new User(userId, User.Status.BASE);
        userRepository.addUser(user);

        Optional<User> optUser = userRepository.findUser(userId);
        assertTrue(optUser.isPresent());
        assertEquals(optUser.get(), user);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUser() {
        Long userId = 1L;
        User user = new User(userId, null);
        userRepository.addUser(user);

        user.setStatus(User.Status.TRACK_LINK);
        userRepository.updateUser(user);
        Optional<User> optUser = userRepository.findUser(userId);

        assertTrue(optUser.isPresent());
        assertEquals(optUser.get(), user);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUserNotExisting() {
        Long userId = 1L;
        User user = new User(userId, User.Status.UNTRACK_LINK);

        assertThrows(UserIdNotFoundException.class, () -> userRepository.updateUser(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllUsers() {
        User user = new User(1L, User.Status.BASE);
        userRepository.addUser(user);

        List<User> users = userRepository.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.getFirst());
    }
}
