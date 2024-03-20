package edu.java.service;

import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
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
public class JdbcUserServiceTest extends IntegrationEnvironment {
    @Autowired
    private UserService userService;
    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    public void testAddUser() {
        User user = new User(1L, null);
        userService.addUser(user);

        Optional<User> optUser = userRepository.findUser(user.getUserId());
        assertTrue(optUser.isPresent());
        assertEquals(user, optUser.get());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUser() {
        User user = new User(1L, null);
        userService.addUser(user);

        assertEquals(userService.findUser(user.getUserId()), user);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUserThatNotExists() {
        assertThrows(UserIdNotFoundException.class, () -> userService.findUser(1L));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveUser() {
        User user = new User(1L, null);
        userService.addUser(user);
        userService.removeUser(user.getUserId());
        assertThrows(UserIdNotFoundException.class, () -> userService.findUser(user.getUserId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUser() {
        User user = new User(1L, null);
        userService.addUser(user);

        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);

        assertEquals(userService.findUser(user.getUserId()).getStatus(), user.getStatus());
        assertEquals(userService.findUser(user.getUserId()).getUserId(), user.getUserId());
    }
}
