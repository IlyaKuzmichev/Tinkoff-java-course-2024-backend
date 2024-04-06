package edu.java.kafka;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@Disabled
@SpringBootTest
public class KafkaScrapperManualTest {
    @Autowired
    private KafkaTemplate<Long, LinkUpdateRequest> linkUpdateRequestKafkaTemplate;
    @Autowired
    private ApplicationConfig config;

    @Test
    public void testKafkaProducerCorrectMessage() {
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            URI.create("http://example.com"),
            "description",
            List.of(754277913L)
        );

        linkUpdateRequestKafkaTemplate.send(
            config.kafkaConfig().topicLinkUpdates().name(),
            request.id(),
            request
        );
    }

    @Test
    public void testMultiplyMessagesKafkaProducerCorrectMessageAndManuallyCheckInBotRuntime() {
        for (int i = 0; i < 10; ++i) {
            LinkUpdateRequest request = new LinkUpdateRequest(
                (long) (i + 1),
                URI.create("%d-example.com".formatted(i)),
                "description + %d".formatted(i * 3),
                List.of(754277913L)
            );

            linkUpdateRequestKafkaTemplate.send(
                config.kafkaConfig().topicLinkUpdates().name(),
                request.id(),
                request
            );
        }
    }
}
