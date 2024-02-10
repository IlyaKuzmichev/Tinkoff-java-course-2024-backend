package edu.java.bot.database;

import java.util.HashSet;

public class User {
    private final Long id;
    private final HashSet<String> links;
    private UserState state;

    public User(Long id) {
        this.id = id;
        links = new HashSet<>();
        state = UserState.BASE;
    }

    public Long getId() {
        return id;
    }

    public HashSet<String> getLinks() {
        return links;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public boolean isTracking(String uri) {
        return links.contains(uri);
    }
}
