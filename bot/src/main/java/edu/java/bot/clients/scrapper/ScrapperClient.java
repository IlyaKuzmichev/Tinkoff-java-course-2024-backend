package edu.java.bot.clients.scrapper;

import edu.java.bot.clients.scrapper.dto.GetStatusResponse;
import edu.java.bot.clients.scrapper.dto.LinkResponse;
import edu.java.bot.clients.scrapper.dto.ListLinksResponse;
import edu.java.bot.clients.scrapper.dto.UserStatus;
import java.net.URI;
import reactor.core.publisher.Mono;

public interface ScrapperClient {
    Mono<Void> registerChat(Long chatId);

    Mono<Void> deleteChat(Long chatId);

    Mono<Void> setUserStatus(Long chatId, UserStatus status);

    Mono<GetStatusResponse> getUserStatus(Long chatId);

    Mono<ListLinksResponse> listLinks(Long chatId);

    Mono<LinkResponse> addLink(Long chatId, URI link);

    Mono<LinkResponse> removeLink(Long chatId, URI link);


}
