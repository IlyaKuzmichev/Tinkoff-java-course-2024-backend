package edu.java.bot.processor.link_trackers;

import edu.java.bot.database.UserRegistry;

public class GitHubTracker extends URITracker {
    public GitHubTracker(UserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public String getDomain() {
        return "github.com";
    }

    @Override
    protected boolean checkUpdate(String uri) {
        return false;
    }
}
