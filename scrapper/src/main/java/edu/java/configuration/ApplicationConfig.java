package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)

public record ApplicationConfig(
    @Bean @NotNull Scheduler scheduler,
    @NotNull AccessType databaseAccessType,
    @NotNull Boolean useQueue,
    KafkaConfig kafkaConfig
) {
    public record Scheduler(boolean enable, @NotNull Duration invokeInterval,
                            @NotNull Duration forceCheckDelay, @NotNull Duration checkInterval) {
    }

    public record KafkaConfig(
        String bootstrapServers,
        TopicLinkUpdates topicLinkUpdates
    ) {
        public record TopicLinkUpdates(
            String name,
            Integer partitions,
            Integer replicas
        ) {
        }

    }
}

