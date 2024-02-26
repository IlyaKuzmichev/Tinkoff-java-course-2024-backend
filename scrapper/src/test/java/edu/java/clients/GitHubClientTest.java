package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.github.WebGitHubClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class GitHubClientTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private WebGitHubClient gitHubClient;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchRepositoryGitHubClient() {
        String repositoryName = "testRepo";
        String ownerName = "testOwner";
        String responseBody = "{\"name\":\"testRepo\",\"full_name\":\"https://github.com/testOwner/testRepo\"," +
            "\"html_url\":\"https://github.com/testOwner/testRepo\",\"updated_at\":\"2022-02-01T00:00:00Z\"," +
            "\"pushed_at\":\"2022-03-01T00:00:00Z\"}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/" + ownerName + "/" + repositoryName))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(gitHubClient.fetchRepository(ownerName, repositoryName))
            .expectNextMatches(response -> {
                OffsetDateTime expectedDate = OffsetDateTime.parse("2022-02-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return response.fullName().equals("https://github.com/testOwner/testRepo") &&
                    response.updatedAt().isEqual(expectedDate);
            })
            .verifyComplete();
    }

    @Test
    public void testFetchRepositoryWithBadRequest() {
        String repositoryName = "invalidRepo";
        String ownerName = "invalidOwner";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/" + ownerName + "/" + repositoryName))
            .willReturn(WireMock.aResponse()
                .withStatus(400)));

        StepVerifier.create(gitHubClient.fetchRepository(ownerName, repositoryName))
            .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.BAD_REQUEST)
            .verify();
    }
}
