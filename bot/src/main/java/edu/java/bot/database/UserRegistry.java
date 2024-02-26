package edu.java.bot.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
public class UserRegistry {
    private final Map<Long, User> users = new HashMap<>();
    @Getter private final Map<String, HashSet<String>> domainLinks = new HashMap<>();
    private final Map<String, Set<User>> usersTrackedLinks = new HashMap<>();

    public UserRegistry() {
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
