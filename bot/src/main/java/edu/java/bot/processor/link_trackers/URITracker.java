package edu.java.bot.processor.link_trackers;

import edu.java.bot.database.UserRegistry;

public abstract class URITracker {
    private URITracker nextTracker;
    protected UserRegistry userRegistry;

    public URITracker(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    public abstract String getDomain();

    protected abstract boolean checkUpdate(String uri);

    protected void execute() {
        for (var link : userRegistry.getDomainLinks().get(getDomain())) {
            if (checkUpdate(link)) {
                notifySubscribers(link);
            }
        }
        if (nextTracker != null) {
            nextTracker.execute();
        }
    }

    public void setNextTracker(URITracker nextTracker) {
        this.nextTracker = nextTracker;
    }

    private void notifySubscribers(String link) {

    }
}
