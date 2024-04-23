package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.service.update_sender.KafkaQueueProducerLinkUpdateSender;
import edu.java.service.update_sender.LinkUpdateSenderService;
import edu.java.service.update_sender.RestClientLinkUpdateSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class LinkUpdateSenderServiceConfig {
    private final ApplicationConfig config;
    private final KafkaTemplate<Long, LinkUpdateRequest> linkUpdateRequestKafkaTemplate;
    private final BotClient botClient;

    @Bean
    public LinkUpdateSenderService linkUpdateSenderService() {
        return config.useQueue() ? new KafkaQueueProducerLinkUpdateSender(
            config,
            linkUpdateRequestKafkaTemplate
        ) : new RestClientLinkUpdateSender(botClient);
    }
}
