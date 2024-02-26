package edu.java.bot.processor.trackers;

public abstract class URITracker {
    private final URITracker nextTracker;

    public URITracker(URITracker nextTracker) {
        this.nextTracker = nextTracker;
    }

    public abstract String getDomain();

    public boolean checkResource(String domain) {
        if (getDomain().equals(domain)) {
            return true;
        }
        if (nextTracker == null) {
            return false;
        }
        return nextTracker.checkResource(domain);
    }
}

