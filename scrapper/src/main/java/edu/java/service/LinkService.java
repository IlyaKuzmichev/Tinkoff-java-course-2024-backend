package edu.java.service;

import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link addLink(Long chatId, URI url);

    Link removeLinkByURL(Long chatId, URI url);

    Collection<Link> findAllLinksForUser(Long chatId);

    Collection<Link> findLinksForUpdate(Long interval);

    LinkInfo updateGithubLink(GithubLinkInfo linkInfo);

    LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo);
}
