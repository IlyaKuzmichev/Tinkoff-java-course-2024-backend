package edu.java.bot.processor;

import edu.java.bot.processor.link_trackers.TrackerHandler;
import java.net.URI;
import java.net.URISyntaxException;

public class LinkChecker {
    private static final String INVALID_LINK = "INVALID";
    private String host;
    private final TrackerHandler trackerHandler;

    public LinkChecker(String link, TrackerHandler trackerHandler) {
        this.trackerHandler = trackerHandler;
        try {
            this.host = new URI(link).getHost();
        } catch (URISyntaxException e) {
            this.host = INVALID_LINK;
        }
        if (host == null) {
            host = INVALID_LINK;
        }
    }


    public boolean isValidLink() {
        return !host.equals(INVALID_LINK);
    }

    public String getHost() {
        return host;
    }

    public boolean isPossibleToTrack() {
        return trackerHandler.isValidResourceForTracking(host);
    }
}
