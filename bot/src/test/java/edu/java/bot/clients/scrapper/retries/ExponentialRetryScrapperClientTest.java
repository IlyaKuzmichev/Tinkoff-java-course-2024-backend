package edu.java.bot.clients.scrapper.retries;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.clients.scrapper.RestScrapperClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.time.OffsetDateTime;

@SpringBootTest
@Slf4j
@DirtiesContext
@TestPropertySource(locations = "classpath:test")
public class ExponentialRetryScrapperClientTest {
    private static final String CHAT_ENDPOINT_PREFIX = "/tg-chat/";
    private static final Long chatId = 1984L;

    private static WireMockServer wireMockServer;
    @Autowired
    private RestScrapperClient restScrapperClient;

    @DynamicPropertySource
    static void retryPolicyProperties(DynamicPropertyRegistry registry) {
        registry.add("retry.attempts", () -> "4");
        registry.add("retry.type", () -> "exponential");
    }

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(8088);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testRetriesWorkingWithExistingStatusCode() {
        log.debug("Start of exponential retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restScrapperClient.registerChat(chatId))
            .verifyError();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId)));
        log.debug("End of exponential retry test");
        log.debug("Time: %s".formatted(OffsetDateTime.now().toString()));
    }
}
