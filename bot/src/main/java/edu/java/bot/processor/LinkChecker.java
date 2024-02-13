package edu.java.bot.processor;

import edu.java.bot.processor.trackers.URITracker;
import java.net.URI;
import java.net.URISyntaxException;

public class LinkChecker {
    private static final String INVALID_LINK = "INVALID";
    private String host;
    private final URITracker trackers;

    public LinkChecker(URITracker trackers) {

        this.trackers = trackers;
    }

    public void loadLink(String link) {
        try {
            host = new URI(link).getHost();
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
        return trackers.checkResource(host);
    }
}
