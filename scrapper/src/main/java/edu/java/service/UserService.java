package edu.java.service;

import edu.java.models.Link;
import edu.java.models.User;
import java.util.List;

public interface UserService {
    void addUser(User user);

    User findUser(Long userId);

    void removeUser(Long userId);

    void updateUser(User user);

    List<Long> getUsersTrackLink(Link link);
}
