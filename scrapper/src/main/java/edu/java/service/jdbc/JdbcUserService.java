package edu.java.service.jdbc;

import edu.java.domain.users.JdbcUserRepository;
import edu.java.models.User;
import edu.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserService implements UserService {
    private final JdbcUserRepository userRepository;

    @Autowired
    public JdbcUserService(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
        user.setStatus(User.Status.BASE);
    }

    @Override
    public void removeUser(Long userId) {
        userRepository.removeUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
