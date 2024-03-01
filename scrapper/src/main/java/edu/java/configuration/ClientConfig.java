package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.WebGitHubClient;
import edu.java.client.WebStackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubClient(WebClient gitHubWebClient) {
        return new WebGitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient stackOverflowWebClient) {
        return new WebStackOverflowClient(stackOverflowWebClient);
    }
}
