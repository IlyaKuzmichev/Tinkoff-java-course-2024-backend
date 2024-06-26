package edu.java.service.jpa;

import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.JpaUserTrackedLinkRepository;
import edu.java.domain.jpa.entities.UserTrackedLinks;
import edu.java.domain.jpa.entities.Users;
import edu.java.exception.AttemptDoubleRegistrationException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JpaUserService implements UserService {
    private final JpaUserRepository userRepository;
    private final JpaUserTrackedLinkRepository userTrackedLinkRepository;

    public JpaUserService(
        JpaUserRepository userRepository,
        JpaUserTrackedLinkRepository userTrackedLinkRepository
    ) {
        this.userRepository = userRepository;
        this.userTrackedLinkRepository = userTrackedLinkRepository;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        Long userId = user.getUserId();
        var optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            throw new AttemptDoubleRegistrationException(userId);
        }

        Users newUser = new Users(userId, User.Status.BASE, new ArrayList<>());
        userRepository.saveAndFlush(newUser);
        user.setStatus(User.Status.BASE);
    }

    @Override
    @Transactional
    public User findUser(Long userId) {
        Users user = checkUserExistence(userId);
        return new User(user.getId(), user.getUserStatus());
    }

    @Override
    @Transactional
    public void removeUser(Long userId) {
        checkUserExistence(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        Users existedUser = checkUserExistence(user.getUserId());
        existedUser.setUserStatus(user.getStatus());
        userRepository.saveAndFlush(existedUser);
    }

    @Override
    public List<Long> getUsersTrackLink(Link link) {
        return userTrackedLinkRepository.findAllByLinkId(link.getId())
            .stream()
            .map(UserTrackedLinks::getUser)
            .map(Users::getId)
            .toList();
    }

    private Users checkUserExistence(Long userId) {
        var optUser = userRepository.findUsersById(userId);

        return optUser.orElseThrow(() -> new UserIdNotFoundException(userId));
    }

}
