package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    KafkaConfig kafkaConfig
) {
    public record KafkaConfig (
        String bootstrapServers,
        TopicLinkUpdates topicLinkUpdates,
        TopicLinkUpdatesDlq topicLinkUpdatesDlq
    ) {
        public record TopicLinkUpdates (
            String name
        ) {}

        public record TopicLinkUpdatesDlq (
            String name,
            Integer partitions,
            Integer replicas
        ) {}
    }
}
