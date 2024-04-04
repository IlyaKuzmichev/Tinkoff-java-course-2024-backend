package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.bot.RestBotClient;
import edu.java.clients.exception.CustomClientException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.scrapper.IntegrationEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
@DirtiesContext
public class RestBotClientTest extends IntegrationEnvironment {
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
    public void testSendUpdatesCorrectRequest() throws URISyntaxException {
        URI url = new URI("https://github.com");

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        StepVerifier.create(restBotClient.sendUpdates(id, url, DESCRIPTION, chatIds))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
    }

    @Test
    public void testSendUpdatesClientError() throws URISyntaxException {
        URI url = new URI("https://example.com");
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

    @Test
    public void testRetriesWorkingWithExistingStatusCode() throws URISyntaxException {
        URI url = new URI("https://example.com");
        log.debug("Start of linear retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));


        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_BAD_GATEWAY)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restBotClient.sendUpdates(id, url, DESCRIPTION, chatIds))
            .verifyError();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
        log.debug("End of linear retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));
    }

    @Test
    public void testRetriesNotWorkingWithNotExistingStatusCode() throws URISyntaxException {
        URI url = new URI("https://example.com");
        log.debug("Start of linear retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));


        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(UPDATES_ENDPOINT))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_IMPLEMENTED)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restBotClient.sendUpdates(id, url, DESCRIPTION, chatIds))
            .verifyError();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(UPDATES_ENDPOINT)));
        log.debug("End of linear retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));
    }
}
