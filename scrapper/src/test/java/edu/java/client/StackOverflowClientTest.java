package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class StackOverflowClientTest {

    private static WireMockServer wireMockServer;

    @Autowired
    private WebStackOverflowClient stackOverflowClient;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchQuestionStackOverflowClient() {
        int questionId = 12345;
        String responseBody = "{\"items\": [{\"question_id\": 12345, \"last_activity_date\": 1687479446}]}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/questions/" + questionId + "?site=stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(stackOverflowClient.fetchQuestion(questionId))
            .expectNextMatches(response -> {
                OffsetDateTime expectedDate = OffsetDateTime.parse("2023-06-23T00:17:26Z");
                return response.isPresent() && response.get().questionId() == questionId &&
                    response.get().getLastActivityDate().isEqual(expectedDate);
            })
            .verifyComplete();
    }

    @Test
    public void testFetchQuestionWithBadRequest() {
        int questionId = -1;

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/questions/" + questionId + "?site=stackoverflow"))
            .willReturn(WireMock.aResponse()
                .withStatus(400)));

        StepVerifier.create(stackOverflowClient.fetchQuestion(questionId))
            .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.BAD_REQUEST)
            .verify();
    }
}
