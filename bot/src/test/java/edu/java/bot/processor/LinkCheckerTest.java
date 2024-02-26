package edu.java.bot.processor;

import edu.java.bot.processor.trackers.GitHubTracker;
import edu.java.bot.processor.trackers.StackOverflowTracker;
import edu.java.bot.processor.trackers.URITracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkCheckerTest {
    LinkChecker checker;
    URITracker trackers;

    @BeforeEach
    public void setUp() {
        trackers = new GitHubTracker(new StackOverflowTracker(null));
        checker = new LinkChecker(trackers);
    }

    @Test
    public void testValidGitHubLink() {
        final String link = "https://github.com/pengrad/java-telegram-bot-api?tab=readme-ov-file#available-methods";
        checker.loadLink(link);
        assertTrue(checker.isValidLink());
        assertEquals("github.com", checker.getHost());
        assertTrue(checker.isPossibleToTrack());
    }

    @Test
    public void testValidStackOverflowLink() {
        final String link = "https://stackoverflow.com/questions/19102180/how-does-gldrawarrays-know-what-to-draw";
        checker.loadLink(link);
        assertTrue(checker.isValidLink());
        assertEquals("stackoverflow.com", checker.getHost());
        assertTrue(checker.isPossibleToTrack());
    }

    @Test
    public void testInvalidDomainAndValidLink() {
        final String link = "https://edu.21-school.ru/calendar";
        checker.loadLink(link);
        assertTrue(checker.isValidLink());
        assertNotEquals("github.com", checker.getHost());
        assertNotEquals("stackoverflow.com", checker.getHost());
        assertFalse(checker.isPossibleToTrack());
    }

    @Test
    public void testInvalidLink() {
        final String link = "Avada-kedavra.us";
        checker.loadLink(link);
        assertFalse(checker.isValidLink());
        assertEquals("INVALID", checker.getHost());
        assertFalse(checker.isPossibleToTrack());
    }
}
