package edu.java.service.jooq;

import edu.java.domain.jooq.JooqUserRepository;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqUserService implements UserService {
    private final JooqUserRepository userRepository;

    @Autowired
    public JooqUserService(JooqUserRepository userRepository) {
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
