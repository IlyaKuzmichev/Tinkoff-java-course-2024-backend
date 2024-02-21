package edu.java.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class LinkUpdaterScheduler {
    static int counter = 1;
    @Scheduled(fixedDelayString = "#{@scheduler.interval().toMillis()}")
    public void update() {
        log.info("Scheduled method update number: " + counter);
        ++counter;
    }
}
