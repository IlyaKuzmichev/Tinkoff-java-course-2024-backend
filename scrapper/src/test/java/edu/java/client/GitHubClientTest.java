package edu.java.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.time.OffsetDateTime;

public class GitHubClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private GitHubClient gitHubClient;

    @Before
    public void setUp() {
        String baseUrl = wireMockRule.baseUrl();
        WebClient.Builder webClientBuilder = WebClient.builder();
        gitHubClient = new WebClientGitHubClient(webClientBuilder, baseUrl);
    }

    @Test
    public void testFetchRepositoryGitHubClient() {
        String repositoryName = "testRepo";
        String ownerName = "testOwner";
        String responseBody = "{\"name\":\"testRepo\",\"description\":\"Test repository\",\"html_url\":\"https://github.com/testOwner/testRepo\",\"updated_at\":\"2022-02-01T00:00:00Z\",\"pushed_at\":\"2022-03-01T00:00:00Z\"}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/" + ownerName + "/" + repositoryName))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(gitHubClient.fetchRepository(ownerName, repositoryName))
            .expectNextMatches(response -> response.getName().equals("testRepo")
                && response.getUpdatedAt().equals(OffsetDateTime.parse("2022-03-01T00:00:00Z")))
            .verifyComplete();
    }
}
