package edu.java.service;

import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.entities.Users;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jpa.JpaUserService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@TestPropertySource(locations = "classpath:test")
public class JpaUserServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaUserService userService;
    @Autowired
    private JpaUserRepository userRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddUserToRepository() {
        Long userId = 321L;
        User user = new User(userId, null);

        userService.addUser(user);
        var userFound = userRepository.findUsersById(userId);

        assertTrue(userFound.isPresent());
        assertEquals(userId, userFound.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUserInRepository() {
        Long userId = 12L;
        User.Status userStatus = User.Status.BASE;
        userRepository.saveAndFlush(new Users(userId, userStatus, new ArrayList<>()));

        User user = userService.findUser(userId);
        assertEquals(userId, user.getUserId());
        assertEquals(userStatus, user.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveUserFromRepository() {
        Long userId = 12L;
        User.Status userStatus = User.Status.BASE;
        userRepository.saveAndFlush(new Users(userId, userStatus, new ArrayList<>()));

        userService.removeUser(userId);
        assertTrue(userRepository.findById(userId).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUserInRepository() {
        Long userId = 12L;
        User.Status userStatus = User.Status.BASE;
        userRepository.saveAndFlush(new Users(userId, userStatus, new ArrayList<>()));

        userService.updateUser(new User(userId, User.Status.TRACK_LINK));

        Optional<Users> optUser = userRepository.findById(userId);
        assertTrue(optUser.isPresent());
        assertEquals(User.Status.TRACK_LINK, optUser.get().getUserStatus());

        userService.updateUser(new User(userId, User.Status.UNTRACK_LINK));

        optUser = userRepository.findById(userId);
        assertTrue(optUser.isPresent());
        assertEquals(User.Status.UNTRACK_LINK, optUser.get().getUserStatus());
    }
}
