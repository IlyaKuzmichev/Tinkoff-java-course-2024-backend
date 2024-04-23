package edu.java.clients.retries.backoff;

import java.time.Duration;

public interface Backoff {
    Duration calculateWaitingTime(int attempt);
}
