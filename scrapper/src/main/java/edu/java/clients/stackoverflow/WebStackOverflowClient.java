package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.StackOverflowQuestionResponse;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebStackOverflowClient implements StackOverflowClient {

    private final WebClient webClient;

    public WebStackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Optional<StackOverflowQuestionResponse>> fetchQuestion(int questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.StackOverflowQuestionResponseList.class)
            .map(resp -> Optional.ofNullable(!resp.items().isEmpty() ? resp.items().getFirst() : null));
    }
}
