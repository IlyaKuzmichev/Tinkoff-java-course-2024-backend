package edu.java.bot.clients.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import java.net.URI;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@DirtiesContext
@TestPropertySource(locations = "classpath:test")
public class RestScrapperClientTest {
    private static final String CHAT_ENDPOINT_PREFIX = "/tg-chat/";
    private static final String LINKS_ENDPOINT = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final Long chatId = 1984L;

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
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restScrapperClient.registerChat(chatId))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId)));
    }

    @Test
    public void testAttemptDoubleRegistrationClientError() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_CONFLICT)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Conflict\", \"code\": \"409\"}")));

        StepVerifier.create(restScrapperClient.registerChat(chatId))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Conflict") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("409"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId)));
    }

    @Test
    public void testDeleteChatCorrectRequest() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        StepVerifier.create(restScrapperClient.registerChat(chatId))
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId)));
    }

    @Test
    public void testAttemptToDeleteChatThatIsNotRegisteredClientError() {
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_ACCEPTABLE)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Not Acceptable\", \"code\": \"406\", \"exceptionName\": \"ChatIdNotFoundException\", \"exceptionMessage\": \"Chat id for user not found\"}")));

        StepVerifier.create(restScrapperClient.deleteChat(chatId))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Not Acceptable") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("406") &&
                ((CustomClientException) throwable).getClientErrorResponse().exceptionName().equals("ChatIdNotFoundException") &&
                ((CustomClientException) throwable).getClientErrorResponse().exceptionMessage().equals("Chat id for user not found"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(CHAT_ENDPOINT_PREFIX + chatId)));
    }

    @Test
    public void testListLinksCorrectRequest() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"links\": [{\"id\": 1, \"url\": \"https://example.com\"}], \"size\": 1}")));
        StepVerifier.create(restScrapperClient.listLinks(chatId))
            .expectNextMatches(response -> {
                assertThat(response.links()).hasSize(1);
                assertThat(response.links().getFirst().id()).isEqualTo(1);
                assertEquals(response.links().getFirst().url().toString(), "https://example.com");
                assertEquals(response.size(), 1);
                return true;
            })
                .verifyComplete();

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/links")).withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString())));
    }

    @Test
    public void testListLinksIncorrectRequestClientError() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_BAD_REQUEST)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Bad Request\", \"code\": \"400\"}")));

        StepVerifier.create(restScrapperClient.listLinks(chatId))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Bad Request") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("400"))
            .verify();

        WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT)).withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString())));
    }

    @Test
    public void testAddLinkCorrectRequest() {
        URI link = URI.create("https://example.com");
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"id\": 1, \"url\": \"https://example.com\"}")));

        StepVerifier.create(restScrapperClient.addLink(chatId, link))
            .expectNextMatches(response -> {
                assertThat(response.id()).isEqualTo(1);
                assertThat(response.url().toString()).isEqualTo("https://example.com");
                return true;
            })
            .verifyComplete();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}")));
    }

    @Test
    public void testAttemptToAddLinkAlreadyTrackingClientError() {
        URI link = URI.create("https://example.com");
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_BAD_REQUEST)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Bad Request\", \"code\": \"400\"}")));

        StepVerifier.create(restScrapperClient.addLink(chatId, link))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Bad Request") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("400"))
            .verify();

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}")));
    }

    @Test
    public void testRemoveLinkCorrectRequest() {
        URI link = URI.create("https://example.com");
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"id\": 1, \"url\": \"https://example.com\"}")));

        StepVerifier.create(restScrapperClient.removeLink(chatId, link))
            .expectNextMatches(response -> {
                assertThat(response.id()).isEqualTo(1);
                assertThat(response.url().toString()).isEqualTo("https://example.com");
                return true;
            })
            .verifyComplete();

        WireMock.verify(WireMock.deleteRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}")));
    }

    @Test
    public void testAttemptToRemoveLinkIsNotTrackingClientError() {
        URI link = URI.create("https://example.com");
        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}"))
            .willReturn(WireMock.aResponse()
                .withStatus(HttpStatus.SC_NOT_FOUND)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\": \"Not Found\", \"code\": \"404\"}")));

        StepVerifier.create(restScrapperClient.removeLink(chatId, link))
            .expectErrorMatches(throwable -> throwable instanceof CustomClientException &&
                ((CustomClientException) throwable).getClientErrorResponse().description().equals("Not Found") &&
                ((CustomClientException) throwable).getClientErrorResponse().code().equals("404"))
            .verify();

        WireMock.verify(WireMock.deleteRequestedFor(WireMock.urlEqualTo(LINKS_ENDPOINT))
            .withHeader(TG_CHAT_ID_HEADER, WireMock.equalTo(chatId.toString()))
            .withRequestBody(equalToJson("{\"link\": \"https://example.com\"}")));
    }
}

