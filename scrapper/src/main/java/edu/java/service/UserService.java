package edu.java.service;

import edu.java.models.User;

public interface UserService {
    void addUser(User user);

    void removeUser(Long userId);

    void updateUser(User user);
}
