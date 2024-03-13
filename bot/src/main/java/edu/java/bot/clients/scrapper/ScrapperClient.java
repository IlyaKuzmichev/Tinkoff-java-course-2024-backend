package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.dto.LinkResponse;
import edu.java.bot.clients.scrapper.dto.ListLinksResponse;
import java.net.URI;
import edu.java.bot.clients.scrapper.dto.UserStatus;
import reactor.core.publisher.Mono;

public interface ScrapperClient {
    Mono<Void> registerChat(Long chatId);

    Mono<Void> deleteChat(Long chatId);

    Mono<Void> setUserStatus(Long chatId, UserStatus status);

    Mono<UserStatus> getUserStatus(Long chatId);

    Mono<ListLinksResponse> listLinks(Long chatId);

    Mono<LinkResponse> addLink(Long chatId, URI link);

    Mono<LinkResponse> removeLink(Long chatId, URI link);


}
