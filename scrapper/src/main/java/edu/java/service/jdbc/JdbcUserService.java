package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.service.UserService;
import java.util.List;
import java.util.Optional;

public class JdbcUserService implements UserService {
    private final JdbcUserRepository userRepository;

    public JdbcUserService(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
        user.setStatus(User.Status.BASE);
    }

    @Override
    public User findUser(Long userId) {
        Optional<User> user = userRepository.findUser(userId);

        if (user.isEmpty()) {
            throw new UserIdNotFoundException(userId);
        }
        return user.get();
    }

    @Override
    public void removeUser(Long userId) {
        userRepository.removeUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public List<Long> getUsersTrackLink(Link link) {
        return userRepository.findUsersTrackLink(link);
    }
}
