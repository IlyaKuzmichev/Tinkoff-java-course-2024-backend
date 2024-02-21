package edu.java.client;

import edu.java.model.StackOverflowQuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebStackOverflowClient implements StackOverflowClient {

    private final WebClient webClient;

    @Autowired
    public WebStackOverflowClient(WebClient.Builder webClientBuilder,
        @Value("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<StackOverflowQuestionResponse> fetchQuestion(int questionId) {
        return webClient.get()
            .uri("/questions/{questionId}", questionId)
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }
}
