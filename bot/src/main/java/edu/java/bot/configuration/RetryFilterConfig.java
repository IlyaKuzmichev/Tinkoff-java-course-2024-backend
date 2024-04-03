package edu.java.bot.configuration;

import edu.java.bot.clients.scrapper.retries.RetryFilter;
import edu.java.bot.clients.scrapper.retries.backoff.Backoff;
import edu.java.bot.clients.scrapper.retries.backoff.ConstantBackoff;
import edu.java.bot.clients.scrapper.retries.backoff.ExponentialBackoff;
import edu.java.bot.clients.scrapper.retries.backoff.LinearBackoff;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RetryFilterConfig {
    private final RetryConfig retryConfig;

    @Bean
    public Backoff backoff() {
        return switch (retryConfig.type()) {
            case CONSTANT ->  new ConstantBackoff(retryConfig.backoffConfig().constant().initialInterval());
            case LINEAR -> new LinearBackoff(
                retryConfig.backoffConfig().linear().initialInterval(),
                retryConfig.backoffConfig().linear()
                    .maximumInterval()
            );
            case EXPONENTIAL -> new ExponentialBackoff(
                retryConfig.backoffConfig().exponential().initialInterval(),
                retryConfig.backoffConfig().exponential().maximumInterval(),
                retryConfig.backoffConfig().exponential().multiplier()
            );
        };
    }

    @Bean
    public RetryFilter retryFilter(Backoff backoff) {
        return new RetryFilter(backoff, retryConfig.statusCodes(), retryConfig.attempts());
    }
}
