package edu.java.configuration;

import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.WebGitHubClient;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.WebStackOverflowClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubClient(@Qualifier("github_web_client") WebClient gitHubWebClient) {
        return new WebGitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        @Qualifier("stackoverflow_web_client") WebClient stackOverflowWebClient) {
        return new WebStackOverflowClient(stackOverflowWebClient);
    }
}
