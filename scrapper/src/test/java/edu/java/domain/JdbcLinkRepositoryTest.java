package edu.java.domain;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcUserRepository userRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void testAddLink() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");

        List<Link> allLinks = linkRepository.findAllLinks();
        assertEquals(1, allLinks.size());
        assertEquals(link.getUrl().toString().toLowerCase(), allLinks.getFirst().getUrl().toString().toLowerCase());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByUrl() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");
        linkRepository.removeLinkByURL(user.getUserId(), link.getUrl());

        List<Link> allLinks = linkRepository.findAllLinks();
        assertEquals(0, allLinks.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinksForUser() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link1 = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        Link link2 = new Link(null, URI.create("https://github.com/IlyaKuzmichev/s21_Math"));
        Link link3 = new Link(null, URI.create("https://stackoverflow.com/questions/123"));
        LinkInfo linkInfo1 = new GithubLinkInfo(link1, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        LinkInfo linkInfo2 = new GithubLinkInfo(link2, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        LinkInfo linkInfo3 = new StackoverflowLinkInfo(link3, OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo1, "github");
        linkRepository.addLink(user.getUserId(), linkInfo2, "github");
        linkRepository.addLink(user.getUserId(), linkInfo3, "stackoverflow");

        List<Link> allLinks = linkRepository.findAllLinksForUser(user.getUserId());
        assertEquals(3, allLinks.size());
        assertEquals(link1.getUrl().toString().toLowerCase(), allLinks.getFirst().getUrl().toString().toLowerCase());
        assertEquals(link2.getUrl().toString().toLowerCase(), allLinks.get(1).getUrl().toString().toLowerCase());
        assertEquals(link3.getUrl().toString().toLowerCase(), allLinks.get(2).getUrl().toString().toLowerCase());
    }
}
