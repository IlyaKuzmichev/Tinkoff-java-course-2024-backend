package edu.java.bot.configuration;

import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record RetryConfig(
    Integer attempts,
    Set<Integer> statusCodes,
    RetryType type,
    BackoffConfig backoffConfig
) {
    public enum RetryType {
        CONSTANT, LINEAR, EXPONENTIAL
    }

    public record BackoffConfig(
        ConstantConfig constant,
        LinearConfig linear,
        ExponentialConfig exponential
    ) {
        public record ConstantConfig(
            Duration initialInterval
        ) {
        }

        public record LinearConfig(
            Duration initialInterval,
            Duration maximumInterval
        ) {
        }

        public record ExponentialConfig(
            Duration initialInterval,
            Duration maximumInterval,
            Double multiplier
        ) {
        }
    }
}
