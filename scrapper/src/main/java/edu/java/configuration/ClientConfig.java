package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.WebGitHubClient;
import edu.java.client.WebStackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder webClientBuilder,
        @Value("${client.github.base-url:https://api.github.com}") String baseUrl) {
        return new WebGitHubClient(webClientBuilder, baseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient.Builder webClientBuilder,
        @Value("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl) {
        return new WebStackOverflowClient(webClientBuilder, baseUrl);
    }
}
