package edu.java.clients.retries.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class LinearBackoff implements Backoff {
private final Duration initialInterval;
private final Duration maximumInterval;

@Override
    public Duration calculateWaitingTime(int attempt) {
        Duration newDelay = initialInterval.multipliedBy(attempt);
        return newDelay.compareTo(maximumInterval)  < 0 ? newDelay : maximumInterval;
    }
}
