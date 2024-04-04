package edu.java.configuration;

import edu.java.clients.retries.RetryFilter;
import edu.java.clients.retries.backoff.Backoff;
import edu.java.clients.retries.backoff.ConstantBackoff;
import edu.java.clients.retries.backoff.ExponentialBackoff;
import edu.java.clients.retries.backoff.LinearBackoff;
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
