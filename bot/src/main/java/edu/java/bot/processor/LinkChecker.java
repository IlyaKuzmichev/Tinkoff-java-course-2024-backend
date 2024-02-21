package edu.java.bot.processor;

import edu.java.bot.processor.trackers.URITracker;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class LinkChecker {
    private static final String INVALID_LINK = "INVALID";
    @Getter private String host;
    private final URITracker trackers;

    public LinkChecker(URITracker trackers) {

        this.trackers = trackers;
    }

    public void loadLink(String link) {
        try {
            host = new URI(link).getHost();
        } catch (URISyntaxException | NullPointerException e) {
            this.host = INVALID_LINK;
        }
        if (host == null) {
            host = INVALID_LINK;
        }
    }

    public boolean isValidLink() {
        return !host.equals(INVALID_LINK);
    }

    public boolean isPossibleToTrack() {
        return trackers.checkResource(host);
    }
}
