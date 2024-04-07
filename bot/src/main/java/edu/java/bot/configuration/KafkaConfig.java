package edu.java.bot.configuration;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {
    private final ApplicationConfig config;

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
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
            ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
            JsonDeserializer.TRUSTED_PACKAGES, "*",
            JsonDeserializer.USE_TYPE_INFO_HEADERS, false,
            JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class
        )));

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
            linkUpdatesDlqKafkaTemplate(linkUpdatesRequestDlqProducerFactory()),
            (r, e) -> new TopicPartition(config.kafkaConfig().topicLinkUpdatesDlq().name(),
                r.partition() % config.kafkaConfig().topicLinkUpdatesDlq().partitions()));

        factory.setCommonErrorHandler(new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0L)));

        return factory;
    }

    @Bean
    ProducerFactory<?, ?> linkUpdatesRequestDlqProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaConfig().bootstrapServers(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class
        ));
    }

    @Bean
    public KafkaTemplate<?, ?> linkUpdatesDlqKafkaTemplate(
        ProducerFactory<?, ?> linkUpdatesRequestDlqProducerFactory
    ) {
        return new KafkaTemplate<>(linkUpdatesRequestDlqProducerFactory);
    }
}
