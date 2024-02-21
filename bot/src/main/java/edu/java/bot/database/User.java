package edu.java.bot.database;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private final Long id;
    private final Set<String> links = new HashSet<>();
    private UserState state = UserState.BASE;

    public User(Long id) {
        this.id = id;
    }
    public boolean isTracking(String uri) {
        return links.contains(uri);
    }
}
