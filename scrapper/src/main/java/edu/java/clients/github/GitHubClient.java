package edu.java.clients.github;

import edu.java.model.GitHubRepositoryResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<GitHubRepositoryResponse> fetchRepository(String ownerName, String repositoryName);
}
