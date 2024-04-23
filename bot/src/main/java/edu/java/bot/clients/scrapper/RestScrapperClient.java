package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.dto.AddLinkRequest;
import edu.java.bot.clients.scrapper.dto.ClientErrorResponse;
import edu.java.bot.clients.scrapper.dto.GetStatusResponse;
import edu.java.bot.clients.scrapper.dto.LinkResponse;
import edu.java.bot.clients.scrapper.dto.ListLinksResponse;
import edu.java.bot.clients.scrapper.dto.RemoveLinkRequest;
import edu.java.bot.clients.scrapper.dto.SetStatusRequest;
import edu.java.bot.clients.scrapper.dto.UserStatus;
import edu.java.bot.clients.scrapper.exception.CustomClientException;
import edu.java.bot.clients.scrapper.retries.RetryFilter;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RestScrapperClient implements ScrapperClient {
    private static final String LINKS_ENDPOINT = "/links";
    private static final String CHAT_ENDPOINT_PREFIX = "/tg-chat/";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String STATUS = "/tg-chat/%d/status";
    private final WebClient webClient;

    @Autowired
    public RestScrapperClient(
        WebClient.Builder webClientBuilder,
        RetryFilter retryFilter,
        @Value("${client.scrapper.base-url:http://localhost:8080}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
            .filter(retryFilter)
            .build();
        log.info(baseUrl);
        log.info(webClient.getClass().getName());
    }

    @Override
    public Mono<Void> registerChat(Long chatId) {
        return webClient
            .post()
            .uri(CHAT_ENDPOINT_PREFIX + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> deleteChat(Long chatId) {
        return webClient
            .delete()
            .uri(CHAT_ENDPOINT_PREFIX + chatId)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> setUserStatus(Long chatId, UserStatus status) {
        return webClient
            .put()
            .uri(STATUS.formatted(chatId))
            .bodyValue(new SetStatusRequest(status))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
            response.bodyToMono(ClientErrorResponse.class)
                .flatMap(clientErrorResponse ->
                    Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(Void.class);
    }

    @Override
    public Mono<GetStatusResponse> getUserStatus(Long chatId) {
        return webClient
            .get()
            .uri(STATUS.formatted(chatId))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(GetStatusResponse.class);
    }

    @Override
    public Mono<ListLinksResponse> listLinks(Long chatId) {
        return webClient
            .get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(ListLinksResponse.class);
    }

    @Override
    public Mono<LinkResponse> addLink(Long chatId, URI link) {
        return webClient
            .post()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .bodyValue(new AddLinkRequest(link))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> removeLink(Long chatId, URI link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .body(Mono.just(new RemoveLinkRequest(link)), RemoveLinkRequest.class)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response ->
                response.bodyToMono(ClientErrorResponse.class)
                    .flatMap(clientErrorResponse ->
                        Mono.error(new CustomClientException(clientErrorResponse))))
            .bodyToMono(LinkResponse.class);
    }
}
