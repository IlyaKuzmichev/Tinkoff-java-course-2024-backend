package edu.java.bot.database;

import lombok.Getter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class UserRegistry {
    private final HashMap<Long, User> users;
    @Getter private final HashMap<String, HashSet<String>> domainLinks;
    private final HashMap<String, HashSet<User>> usersTrackedLinks;

    public UserRegistry() {
        users = new HashMap<>();
        domainLinks = new HashMap<>();
        usersTrackedLinks = new HashMap<>();
    }

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public void putUser(User user) {
        users.put(user.getId(), user);
    }

    public boolean addLink(User user, String domain, String link) {
        boolean success = user.getLinks().add(link);
        if (!success) {
            return false;
        }
        usersTrackedLinks.computeIfAbsent(link, k -> new HashSet<>()).add(user);
        domainLinks.computeIfAbsent(domain, k -> new HashSet<>()).add(link);
        return true;
    }

    public boolean removeLink(User user, String domain, String link) {
        boolean success = user.getLinks().remove(link);
        if (!success) {
            return false;
        }
        var usersTracking = usersTrackedLinks.get(link);
        usersTracking.remove(user);
        if (usersTracking.isEmpty()) {
            domainLinks.get(domain).remove(link);
        }
        return true;
    }
}
