package edu.java.domain;

import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcUserRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    void testAddUser() {
        User user = new User(123456789L, User.Status.BASE);
        userRepository.addUser(user);

        List<User> users = userRepository.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveUser() {
        User user = new User(13L, User.Status.BASE);
        userRepository.addUser(user);
        userRepository.removeUser(user.getUserId());

        List<User> users = userRepository.findAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddUserAlreadyExists() {
        User user = new User(111111111L, null);
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
        User user = new User(13L, User.Status.BASE);
        userRepository.addUser(user);
        Optional<User> optUser = userRepository.findUser(13L);

        assertTrue(optUser.isPresent());
        assertEquals(user, optUser.get());
    }
}
