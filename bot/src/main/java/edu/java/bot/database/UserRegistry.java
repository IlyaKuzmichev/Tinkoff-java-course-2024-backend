package edu.java.bot.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class UserRegistry {
    private final HashMap<Long, User> users;
    private final HashMap<String, HashSet<String>> domainLinks;
    private final HashMap<String, HashSet<User>> usersTrackedLinks;

    public UserRegistry() {
        users = new HashMap<>();
        domainLinks = new HashMap<>();
        usersTrackedLinks = new HashMap<>();
    }

    public Optional<User> getUser(Long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    public void putUser(User user) {
        users.put(user.getId(), user);
    }

    public HashMap<String, HashSet<String>> getDomainLinks() {
        return domainLinks;
    }

    public boolean addLink(User user, String domain, String link) {
        boolean success = user.getLinks().add(link);
        if (!success) {
            return false;
        }
        if (!usersTrackedLinks.containsKey(link)) {
            usersTrackedLinks.put(link, new HashSet<>());
        }
        usersTrackedLinks.get(link).add(user);
        if (!domainLinks.containsKey(domain)) {
            domainLinks.put(domain, new HashSet<>());
        }
        domainLinks.get(domain).add(link);
        return true;
    }

    public boolean removeLink(User user, String domain, String link) {
        boolean success = user.getLinks().remove(link);
        if (!success) {
            return false;
        }
        usersTrackedLinks.get(link).remove(user);
        if (usersTrackedLinks.get(link).size() == 0) {
            domainLinks.get(domain).remove(link);
        }
        return true;
    }
}
