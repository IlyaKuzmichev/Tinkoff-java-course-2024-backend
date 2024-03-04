package edu.java.domain.links;

import edu.java.controller.dto.ListLinksResponse;
import java.net.URI;

public interface LinkRepository {
    void addLink(Long chatId, URI url);

    void removeLink(Long chatId, URI url);

    ListLinksResponse findAllLinks();
}
