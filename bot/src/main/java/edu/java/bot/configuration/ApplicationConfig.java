package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    KafkaConfig kafkaConfig,
    Micrometer micrometer
) {
    public record KafkaConfig(
        String bootstrapServers,
        TopicLinkUpdates topicLinkUpdates,
        TopicLinkUpdatesDlq topicLinkUpdatesDlq
    ) {
        public record TopicLinkUpdates(
            String name
        ) {}

        public record TopicLinkUpdatesDlq(
            String name,
            Integer partitions,
            Integer replicas
        ) {}
    }

    public record Micrometer(
        ProcessedMessageCounter processedMessageCounter
    ) {
        public record ProcessedMessageCounter(
            String name,
            String description
        ) {}
    }
}
