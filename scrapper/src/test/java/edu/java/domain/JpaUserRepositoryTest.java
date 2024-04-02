package edu.java.domain;

import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.entities.Users;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
public class JpaUserRepositoryTest extends IntegrationEnvironment {
    private static final String INSERT_USER = "INSERT INTO users(id) VALUES (?)";
    private static final String SELECT_USER = "SELECT id FROM users";
    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM users";
    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveUserToRepository() {
        Long userId = 123L;
        Users user = new Users();
        user.setId(userId);
        user.setUserStatus(User.Status.BASE);

        userRepository.saveAndFlush(user);

        assertEquals(jdbcTemplate.queryForObject(SELECT_USER, Long.class), userId);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUserInRepository() {
        Long userId = 123L;
        Users user = new Users();
        user.setId(userId);
        user.setUserStatus(User.Status.BASE);

        jdbcTemplate.update(INSERT_USER, userId);
        Optional<Users> userFound = userRepository.findUsersById(userId);

        assertTrue(userFound.isPresent());
        assertEquals(userFound.get().getId(), userId);
        assertEquals(userFound.get().getUserStatus(), user.getUserStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteUserFromRepository() {
        Long userId = 123L;
        Users user = new Users();
        user.setId(userId);
        user.setUserStatus(User.Status.BASE);

        jdbcTemplate.update(INSERT_USER, userId);
        userRepository.deleteById(userId);
        userRepository.flush();

        assertEquals(0, jdbcTemplate.queryForObject(COUNT_ROWS, Integer.class));
    }
}
