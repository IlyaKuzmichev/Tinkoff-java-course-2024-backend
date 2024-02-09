package edu.java.bot.database;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class UserRegistry {
    private final HashMap<Long, User> users;
//    private final HashMap<URI, HashSet<User>>  usersTrackedLinks;

    public UserRegistry() {
        users = new HashMap<>();
//        usersTrackedLinks = new HashMap<>();
    }

    public Optional<User> getUser(Long id) {
        return  users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    public void putUser(User user) {
        users.put(user.getId(), user);
    }
}
