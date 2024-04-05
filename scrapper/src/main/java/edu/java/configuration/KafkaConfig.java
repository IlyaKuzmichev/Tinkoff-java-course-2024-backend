package edu.java.configuration;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    ApplicationConfig config;
    @Bean
    public NewTopic topicLinkUpdates() {
        return TopicBuilder
            .name(config.kafkaConfig().topicLinkUpdates().name())
            .partitions(config.kafkaConfig().topicLinkUpdates().partitions())
            .replicas(config.kafkaConfig().topicLinkUpdates().replicas())
            .build();
    }

    @Bean
    ProducerFactory<Long, LinkUpdateRequest> linkUpdatesRequestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaConfig().bootstrapServers(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Long.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            JsonSerializer.ADD_TYPE_INFO_HEADERS, false
        ));
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> linkUpdatesKafkaTemplate(
        ProducerFactory<Long, LinkUpdateRequest> linkUpdatesRequestProducerFactory
    ) {
        return new KafkaTemplate<>(linkUpdatesRequestProducerFactory);
    }
}
