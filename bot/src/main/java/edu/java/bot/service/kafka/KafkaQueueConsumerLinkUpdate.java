package edu.java.bot.service.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.UpdateNotifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaQueueConsumerLinkUpdate {
    private final ApplicationConfig config;
    private final UpdateNotifierService updateNotifierService;
    private final KafkaTemplate<Long, byte[]> linkUpdatesDlqKafkaTemplate;

    @KafkaListener(groupId = "listeners.link.update",
                   topics = "${app.kafka-config.topic-link-updates.name}",
                   containerFactory = "linkUpdatesRequestConcurrentKafkaListenerContainerFactory",
                   concurrency = "2")
    public void listenLinkUpdateMessages(@Payload LinkUpdateRequest linkUpdateRequest) {
        log.info("Get new link update from scrapper %s %s".formatted(
            linkUpdateRequest.description(),
            linkUpdateRequest.url()
        ));

        try {
            updateNotifierService.notify(
                linkUpdateRequest.description(),
                linkUpdateRequest.url(),
                linkUpdateRequest.tgChatIds()
            );
        } catch (NullPointerException e) {
            log.error("Error with message parsing from Kafka, some fields of DTO are null");
            log.error(e.getMessage());

            linkUpdatesDlqKafkaTemplate.send(
                config.kafkaConfig().topicLinkUpdatesDlq().name(),
                linkUpdateRequest.id(),
                linkUpdateRequest.toString().getBytes()
            );
        }
    }
}
