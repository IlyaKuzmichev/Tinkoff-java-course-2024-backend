package edu.java.bot.processor.trackers;

public class StackOverflowTracker extends URITracker {
    public StackOverflowTracker(URITracker nextTracker) {
        super(nextTracker);
    }

    @Override
    public String getDomain() {
        return "stackoverflow.com";
    }
}
