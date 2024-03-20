package edu.java.service;

import edu.java.domain.jooq.JooqUserRepository;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jooq.JooqUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JooqUserServiceTest extends IntegrationEnvironment {
    @Autowired
    private JooqUserService userService;
    @Autowired
    private JooqUserRepository userRepository;

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
