package edu.java.bot.processor.link_trackers;

import edu.java.bot.database.UserRegistry;

public class StackOverflowTracker extends URITracker {
    public StackOverflowTracker(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String getDomain() {
        return "stackoverflow.com";
    }

    @Override
    protected boolean checkUpdate(String uri) {
        return false;
    }
}
