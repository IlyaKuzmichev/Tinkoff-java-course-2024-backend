package edu.java.domain.users;

import edu.java.controller.exception.AttemptDoubleRegistrationException;
import edu.java.controller.exception.ChatIdNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private static final String EXCEPTION_PREFIX = "User with chat ID ";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void addUser(Long chatId) {
        String sql = "INSERT INTO users(chat_id) VALUES (?)";
        try {
            jdbcTemplate.update(sql, chatId);
        } catch (DataIntegrityViolationException e) {
            throw new AttemptDoubleRegistrationException(EXCEPTION_PREFIX + chatId + " already exists.");
        }
    }

    @Override
    @Transactional
    public void removeUser(Long chatId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, chatId);
        if (rowsAffected == 0) {
            throw new ChatIdNotFoundException(EXCEPTION_PREFIX + chatId + "not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllUsers() {
        String sql = "SELECT chat_id FROM users";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("chat_id"));
    }
}
