package edu.java.bot.configuration;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {
    ApplicationConfig config;
    @Bean
    public NewTopic topicLinkUpdatesDlq() {
        return TopicBuilder
            .name(config.kafkaConfig().topicLinkUpdatesDlq().name())
            .partitions(config.kafkaConfig().topicLinkUpdatesDlq().partitions())
            .replicas(config.kafkaConfig().topicLinkUpdatesDlq().replicas())
            .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest>
    linkUpdatesRequestConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaConfig().bootstrapServers(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Long.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        )));
        return factory;
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
