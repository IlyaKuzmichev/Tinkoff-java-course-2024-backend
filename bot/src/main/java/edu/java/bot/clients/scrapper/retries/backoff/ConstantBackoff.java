package edu.java.bot.clients.scrapper.retries.backoff;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConstantBackoff implements Backoff {
    private final Duration constant;


    @Override
    public Duration calculateWaitingTime(int attempt) {
        return constant;
    }
}
