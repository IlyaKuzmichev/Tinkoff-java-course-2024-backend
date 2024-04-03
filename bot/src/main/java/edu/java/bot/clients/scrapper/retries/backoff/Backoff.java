package edu.java.bot.clients.scrapper.retries.backoff;

import java.time.Duration;

public interface Backoff {
    Duration calculateWaitingTime(int attempt);
}
