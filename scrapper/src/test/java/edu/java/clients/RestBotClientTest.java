package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.bot.RestBotClient;
import edu.java.clients.exception.CustomClientException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class RestBotClientTest {
    private static final String UPDATES_ENDPOINT = "/updates";
    private static final String DESCRIPTION = "Description";
    private static final Long id = 1L;
    private static final List<Long> chatIds = List.of();

    private static WireMockServer wireMockServer;
    @Autowired
    private RestBotClient restBotClient;

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
    public void testSendUpdatesCorrectRequest() {
        String url = "https://github.com";

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        StepVerifier.create(restBotClient.sendUpdates(id, url, DESCRIPTION, chatIds))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
    }

    @Test
    public void testSendUpdatesClientError() {
        String url = "https://example.com";
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_FOUND)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Not Found\", \"code\": \"404\"}")));

        StepVerifier.create(restBotClient.sendUpdates(id, url, DESCRIPTION, chatIds))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                    ((CustomClientException) throwable).getClientErrorResponse().description().equals("Not Found") &&
                    ((CustomClientException) throwable).getClientErrorResponse().code().equals("404"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
    }
}
