package edu.java.service.update_sender;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j
public class KafkaQueueProducerLinkUpdateSender implements LinkUpdateSenderService {
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> linkUpdateRequestKafkaTemplate;

    @Override
    public void sendUpdate(LinkUpdateRequest updateRequest) {
        log.info("Sending new link update request to Kafka: %d %s %s".formatted(
            updateRequest.id(),
            updateRequest.description(),
            updateRequest.url()
        ));

        linkUpdateRequestKafkaTemplate.send(
            config.kafkaConfig().topicLinkUpdates().name(),
            updateRequest.id(),
            updateRequest
        );
    }
}
