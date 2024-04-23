package edu.java.bot.clients.scrapper.retries.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExponentialBackoff implements Backoff {
    private final Duration initialInterval;
    private final Duration maximumInterval;
    private final Double multiplier;

    @Override
    public Duration calculateWaitingTime(int attempt) {
        Duration newDelay = initialInterval.multipliedBy((long) Math.pow(multiplier, attempt - 1));
        return newDelay.compareTo(maximumInterval)  < 0 ? newDelay : maximumInterval;
    }
}
