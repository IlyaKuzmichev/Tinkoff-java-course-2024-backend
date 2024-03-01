package edu.java.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    WebClient gitHubWebClient(WebClient.Builder webClientBuilder,
        @Value("${client.github.base-url:https://api.github.com}") String baseUrl) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    @Bean
    WebClient stackOverflowWebClient(WebClient.Builder webClientBuilder,
        @Value("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }
}
