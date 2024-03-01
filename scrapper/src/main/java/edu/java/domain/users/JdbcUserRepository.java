package edu.java.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(Long chatId) {
        String sql = "INSERT INTO users(chat_id) VALUES (?)";
        jdbcTemplate.update(sql, chatId);
    }

    @Override
    public void removeUser(Long chatId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, chatId);
    }

    @Override
    public List<Long> findAllUsers() {
        String sql = "SELECT chat_id FROM users";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("chat_id"));
    }
}
