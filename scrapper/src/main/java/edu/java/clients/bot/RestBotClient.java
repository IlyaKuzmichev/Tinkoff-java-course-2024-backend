package edu.java.clients.bot;

import edu.java.clients.bot.dto.ClientErrorResponse;
import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.clients.exception.CustomClientException;
import edu.java.clients.retries.RetryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestBotClient implements BotClient {
    private static final String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;

    @Autowired
    public RestBotClient(
        WebClient.Builder webClientBuilder,
        RetryFilter retryFilter,
        @Value("${client.bot.base-url:http://localhost:8090}") String baseUrl
    ) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .filter(retryFilter)
            .build();
    }

    @Override
    public Mono<Void> sendUpdates(LinkUpdateRequest updateRequest) {
        return webClient
            .post()
            .uri(UPDATES_ENDPOINT)
            .bodyValue(updateRequest)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(Void.class);
    }
}
