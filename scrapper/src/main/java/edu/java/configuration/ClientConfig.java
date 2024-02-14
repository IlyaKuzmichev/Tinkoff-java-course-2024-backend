package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.WebClientGitHubClient;
import edu.java.client.WebClientStackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:application.properties")
public class ClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder webClientBuilder,
        @Value("${github.base-url:https://api.github.com}") String baseUrl) {
        return new WebClientGitHubClient(webClientBuilder, baseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient.Builder webClientBuilder,
        @Value("${stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl) {
        return new WebClientStackOverflowClient(webClientBuilder, baseUrl);
    }
}
