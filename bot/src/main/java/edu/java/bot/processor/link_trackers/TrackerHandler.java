package edu.java.bot.processor.link_trackers;

import edu.java.bot.database.UserRegistry;
import java.util.ArrayList;

public class TrackerHandler {
    private final ArrayList<URITracker> trackerList;

    public TrackerHandler(UserRegistry userRegistry) {
        trackerList = new ArrayList<>();
        trackerList.add(new GitHubTracker(userRegistry));
        trackerList.add(new StackOverflowTracker(userRegistry));

        for (var i = 0; i < trackerList.size() - 1; ++i) {
            trackerList.get(i).setNextTracker(trackerList.get(i + 1));
        }
    }

    public boolean isValidResourceForTracking(String domain) {
        if (domain == null) {
            return false;
        }
        for (var tracker : trackerList) {
            if (tracker.getDomain().equals(domain)) {
                return true;
            }
        }
        return false;
    }

    public void checkUpdatesOnTrackingResources() {
        trackerList.get(0).execute();
    }

    public String[] getDomains() {
        return trackerList.stream()
            .map(URITracker::getDomain)
            .toArray(String[]::new);
    }
}
