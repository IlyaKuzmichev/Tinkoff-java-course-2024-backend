package edu.java.client;

import edu.java.model.GitHubRepositoryResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebGitHubClient implements GitHubClient {
    private final WebClient webClient;

    public WebGitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String ownerName, String repositoryName) {
        return webClient.get()
            .uri("/repos/{ownerName}/{repositoryName}", ownerName, repositoryName)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class);
    }
}
