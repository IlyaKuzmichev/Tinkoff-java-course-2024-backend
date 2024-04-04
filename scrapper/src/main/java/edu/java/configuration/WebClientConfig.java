package edu.java.configuration;

import edu.java.clients.retries.RetryFilter;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("github_web_client")
    WebClient gitHubWebClient(
        WebClient.Builder webClientBuilder,
        RetryFilter retryFilter,
        @Value("${client.github.base-url:https://api.github.com}") String baseUrl
    ) {
        return webClientBuilder
            .baseUrl(baseUrl)
            .filter(retryFilter)
            .build();
    }

    @Bean
    @Qualifier("stackoverflow_web_client")
    WebClient stackOverflowWebClient(
        WebClient.Builder webClientBuilder,
        RetryFilter retryFilter,
        @Value("${client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl
    ) {
        return webClientBuilder
            .baseUrl(baseUrl)
            .filter(retryFilter)
            .build();
    }
}
