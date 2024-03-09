package edu.java.service;

import edu.java.models.Link;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link addLink(Long chatId, URI url);

    Link removeLinkByURL(Long chatId, URI url);

    Collection<Link> findAllLinks();
}
