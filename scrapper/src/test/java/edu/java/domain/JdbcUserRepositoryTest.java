package edu.java.domain;

import edu.java.controller.exception.AttemptDoubleRegistrationException;
import edu.java.controller.exception.ChatIdNotFoundException;
import edu.java.domain.users.UserRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcUserRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    void addUserTest() {
        userRepository.addUser(123456789L);

        List<Long> users = userRepository.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(Long.valueOf(123456789L), users.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    public void removeUserTest() {
        userRepository.addUser(987654321L);
        userRepository.removeUser(987654321L);

        List<Long> users = userRepository.findAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    @Transactional
    @Rollback
    public void addUserAlreadyExistsTest() {
        userRepository.addUser(111111111L);

        assertThrows(AttemptDoubleRegistrationException.class, () -> userRepository.addUser(111111111L));
    }

    @Test
    @Transactional
    @Rollback
    public void removeNonExistingUserTest() {
        assertThrows(ChatIdNotFoundException.class, () -> userRepository.removeUser(999999999L));
    }
}
