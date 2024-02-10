package edu.java.bot.processor.trackers;

public class GitHubTracker extends URITracker {
    public GitHubTracker(URITracker nextTracker) {
        super(nextTracker);
    }

    @Override
    public String getDomain() {
        return "github.com";
    }
}
