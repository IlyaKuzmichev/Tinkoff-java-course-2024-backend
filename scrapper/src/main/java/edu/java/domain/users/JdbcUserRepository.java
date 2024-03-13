package edu.java.domain.users;

import edu.java.domain.mappers.UserStatusMapper;
import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcUserRepository {
    private static final String USER_EXCEPTION_FORMAT = "User with ID %d %s";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addUser(User user) {
        String sql = "INSERT INTO users(id) VALUES (?)";
        try {
            jdbcTemplate.update(sql, user.getUserId());
        } catch (DataIntegrityViolationException e) {
            throw new AttemptDoubleRegistrationException(
                USER_EXCEPTION_FORMAT.formatted(user.getUserId(), "already exists")
            );
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET user_status = ?::user_status_enum WHERE id = ?";
        int affected = jdbcTemplate.update(
            sql,
            UserStatusMapper.userStatusToString(user.getStatus()),
            user.getUserId()
        );
        if (affected == 0) {
            throw new UserIdNotFoundException(user.getUserId());
        }
    }

    @Transactional
    public void removeUser(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId);
        if (rowsAffected == 0) {
            throw new UserIdNotFoundException(userId);
        }
    }

    public Optional<User> findUser(Long userId) {
        String sql = "SELECT id, user_status FROM users WHERE id = ?";
        Optional<User> user;
        try {
            user = Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserMapper(), userId));
        } catch (IncorrectResultSizeDataAccessException e) {
            user = Optional.empty();
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        String sql = "SELECT id, user_status FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Transactional(readOnly = true)
    public List<Long> findUsersTrackLink(Link link) {
        String sql = "SELECT user_id FROM user_tracked_links WHERE link_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
            resultSet.getLong("user_id"), link.getId());
    }

    private static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Long userId = resultSet.getLong("id");
            User.Status status = UserStatusMapper.userStatusFromString(resultSet.getString("user_status"));
            return new User(userId, status);
        }
    }
}