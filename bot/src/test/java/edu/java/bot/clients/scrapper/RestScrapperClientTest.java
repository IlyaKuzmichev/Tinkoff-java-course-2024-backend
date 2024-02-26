package edu.java.bot.clients.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest
@TestPropertySource(locations = "classpath:test")
public class RestScrapperClientTest {
    private static final String CHAT_ENDPOINT_PREFIX = "/tg-chat/";
    private static final String LINKS_ENDPOINT = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final Long id = 1984L;

    private static WireMockServer wireMockServer;
    @Autowired
    private RestScrapperClient restScrapperClient;

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
    public void testRegisterChatCorrectRequest() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restScrapperClient.registerChat(id))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id)));
    }

    @Test
    public void testAttemptDoubleRegistrationClientError() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_CONFLICT)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Conflict\", \"code\": \"409\"}")));

        StepVerifier.create(restScrapperClient.registerChat(id))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Conflict") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("409"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id)));
    }

    @Test
    public void testDeleteChatCorrectRequest() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restScrapperClient.registerChat(id))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id)));
    }

    @Test
    public void testAttemptToDeleteChatThatIsNotRegisteredClientError() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_ACCEPTABLE)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Not Acceptable\", \"code\": \"406\", \"exceptionName\": \"ChatIdNotFoundException\", \"exceptionMessage\": \"Chat id for user not found\"}")));

        StepVerifier.create(restScrapperClient.deleteChat(id))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Not Acceptable") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("406") &&
                ((CustomClientException) throwable).getClientErrorResponse().exceptionName().equals("ChatIdNotFoundException") &&
                ((CustomClientException) throwable).getClientErrorResponse().exceptionMessage().equals("Chat id for user not found"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + id)));
    }
}

