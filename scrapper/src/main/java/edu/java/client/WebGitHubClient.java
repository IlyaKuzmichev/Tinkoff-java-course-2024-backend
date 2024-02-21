package edu.java.client;

import edu.java.model.GitHubRepositoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebGitHubClient implements GitHubClient {
    private final WebClient webClient;

    @Autowired
    public WebGitHubClient(WebClient.Builder webClientBuilder,
        @Value("${client.github.base-url:https://api.github.com}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String ownerName, String repositoryName) {
        return webClient.get()
            .uri("/repos/{ownerName}/{repositoryName}", ownerName, repositoryName)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class);
    }
}
