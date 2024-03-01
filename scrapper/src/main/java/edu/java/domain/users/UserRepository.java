package edu.java.domain.users;

import java.util.List;

public interface UserRepository {
    void addUser(Long chatId);
    void removeUser(Long chatId);
    List<Long> findAllUsers();
}
