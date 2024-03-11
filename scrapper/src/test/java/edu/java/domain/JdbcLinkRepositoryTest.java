package edu.java.domain;

import edu.java.domain.links.JdbcLinkRepository;
import edu.java.domain.users.JdbcUserRepository;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
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
        linkRepository.addLink(user.getUserId(), link);

        List<Link> allLinks = linkRepository.findAllLinks();
        assertEquals(1, allLinks.size());
        assertEquals(link.getUrl(), allLinks.getFirst().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByUrl() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        linkRepository.addLink(user.getUserId(), link);
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
        linkRepository.addLink(user.getUserId(), link1);
        linkRepository.addLink(user.getUserId(), link2);
        linkRepository.addLink(user.getUserId(), link3);

        List<Link> allLinks = linkRepository.findAllLinksForUser(user.getUserId());
        assertEquals(3, allLinks.size());
        assertEquals(link1.getUrl(), allLinks.getFirst().getUrl());
        assertEquals(link2.getUrl(), allLinks.get(1).getUrl());
        assertEquals(link3.getUrl(), allLinks.get(2).getUrl());
    }
}
