package edu.java.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class StackOverflowClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private StackOverflowClient stackOverflowClient;

    @Before
    public void setUp() {
        String baseUrl = wireMockRule.baseUrl();
        WebClient.Builder webClientBuilder = WebClient.builder();
        stackOverflowClient = new WebClientStackOverflowClient(webClientBuilder, baseUrl);
    }

    @Test
    public void testFetchQuestion() {
        int questionId = 12345;
        String responseBody = "{\"title\":\"Test question\",\"question_id\":12345,\"link\":\"https://stackoverflow.com/questions/12345\",\"creation_date\":1643971200,\"last_activity_date\":1643972400}";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/questions/" + questionId))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)));

        StepVerifier.create(stackOverflowClient.fetchQuestion(questionId))
            .expectNextMatches(response -> response.getQuestionId() == 12345)
            .verifyComplete();
    }
}
