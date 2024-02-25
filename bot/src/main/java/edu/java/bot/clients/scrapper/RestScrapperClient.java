package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.dto.AddLinkRequest;
import edu.java.bot.clients.scrapper.dto.LinkResponse;
import edu.java.bot.clients.scrapper.dto.ListLinksResponse;
import edu.java.bot.clients.scrapper.dto.RemoveLinkRequest;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestScrapperClient implements ScrapperClient {
    private static final String LINKS_ENDPOINT = "/links";
    private static final String CHAT_ENDPOINT_PREFIX = "/tg-chat/";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;

    @Autowired
    public RestScrapperClient(WebClient.Builder builder, @Value("${client.scrapper.base-url:http://localhost:8080}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Void> registerChat(Long chatId) {
        return webClient
            .post()
            .uri(CHAT_ENDPOINT_PREFIX + chatId)
            .retrieve()
            .toBodilessEntity()
            .mapNotNull(HttpEntity::getBody);
    }

    @Override
    public Mono<Void> deleteChat(Long chatId) {
        return webClient
            .delete()
            .uri(CHAT_ENDPOINT_PREFIX + chatId)
            .retrieve()
            .toBodilessEntity()
            .mapNotNull(HttpEntity::getBody);
    }

    @Override
    public Mono<ListLinksResponse> listLinks(Long chatId) {
        return webClient
            .get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    @Override
    public Mono<LinkResponse> addLink(Long chatId, URI link) {
        return webClient
            .post()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .bodyValue(new AddLinkRequest(link.toString()))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> removeLink(Long chatId, URI link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, chatId.toString())
            .body(Mono.just(new RemoveLinkRequest(link.toString())), RemoveLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
