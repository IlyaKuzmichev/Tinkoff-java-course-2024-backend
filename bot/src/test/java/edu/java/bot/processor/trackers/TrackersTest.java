package edu.java.bot.processor.trackers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrackersTest {
    @Test
    public void testGitHubTracker() {
        URITracker gitHubTracker = new GitHubTracker(null);

        assertEquals(gitHubTracker.getDomain(), "github.com");
        assertTrue(gitHubTracker.checkResource("github.com"));
    }

    @Test
    public void stackOverflowTracker() {
        URITracker stackOverflowTracker = new StackOverflowTracker(null);
        assertEquals(stackOverflowTracker.getDomain(), "stackoverflow.com");
        assertTrue(stackOverflowTracker.checkResource("stackoverflow.com"));
    }
}
