package edu.java.service;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcUserService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:test", properties = "client.github.base-url=https://api.github.com")
@TestPropertySource(locations = "classpath:test",
                    properties = "client.stackoverflow.base-url=https://api.stackexchange.com/2.3")
//@EnabledIfEnvironmentVariable(named = "SCRAPPER_REAL_TESTS_ENABLE", matches = "1")
public class JdbcLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JdbcUserService userService;
    @Autowired
    private JdbcLinkService linkService;
    @Autowired
    private JdbcLinkRepository linkRepository;
    private static final URI GITHUB_URL = URI.create(
        "https://github.com/IlyaKuzmichev/s21_Info21".toLowerCase());
    private static final URI STACKOVERFLOW_URL = URI.create(
        "https://stackoverflow.com/questions/78179944/pointer-type-vs-value-type-in-golang".toLowerCase());

    @BeforeEach
    public void setUp() {
        userService.addUser(new User(1L, null));
        userService.addUser(new User(2L, null));
        userService.addUser(new User(3L, null));
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLink() {
        User user = userService.findUser(1L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        Link link = linkService.addLink(user.getUserId(), GITHUB_URL);
        List<Link> links = linkRepository.findAllLinksForUser(user.getUserId());

        assertEquals(1, links.size());
        assertEquals(link.getUrl().toString().toLowerCase(), links.getFirst().getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByUrl() {
        User user = userService.findUser(1L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        linkService.addLink(user.getUserId(), GITHUB_URL);

        user.setStatus(User.Status.UNTRACK_LINK);
        userService.updateUser(user);
        Link link = linkService.removeLinkByURL(user.getUserId(), GITHUB_URL);
        List<Link> links = linkRepository.findAllLinksForUser(user.getUserId());
        assertEquals(0, links.size());
        assertEquals(GITHUB_URL, link.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinksForUser() {
        User user = userService.findUser(3L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        linkService.addLink(user.getUserId(), GITHUB_URL);

        Collection<Link> allLinksForUser = linkService.findAllLinksForUser(user.getUserId());
        assertEquals(1, allLinksForUser.size());
        assertEquals(GITHUB_URL, allLinksForUser.stream().toList().getFirst().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindLinksForUpdate() {
        User user = userService.findUser(2L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        linkService.addLink(user.getUserId(), GITHUB_URL);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        linkService.addLink(user.getUserId(), STACKOVERFLOW_URL);

        Collection<Link> updateLinks = linkService.findLinksForUpdate(-1L);
        assertEquals(2, updateLinks.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGithubLink() {
        User user = userService.findUser(1L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        Link link = linkService.addLink(user.getUserId(), GITHUB_URL);

        OffsetDateTime updateTime = OffsetDateTime.now().minusMinutes(10);
        GithubLinkInfo linkInfo = new GithubLinkInfo(link, updateTime, updateTime, 1);
        GithubLinkInfo oldLinkInfo = (GithubLinkInfo) linkService.updateGithubLink(linkInfo);

        assertEquals(link, oldLinkInfo.getLink());

        oldLinkInfo = (GithubLinkInfo) linkService.updateGithubLink(linkInfo);

        assertEquals(link, oldLinkInfo.getLink());
        assertEquals(updateTime, oldLinkInfo.getUpdateTime());
        assertEquals(updateTime, oldLinkInfo.getPushTime());
        assertEquals(1, oldLinkInfo.getPullRequestsCount());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateStackoverflowLink() {
        User user = userService.findUser(1L);
        user.setStatus(User.Status.TRACK_LINK);
        userService.updateUser(user);
        Link link = linkService.addLink(user.getUserId(), STACKOVERFLOW_URL);

        OffsetDateTime updateTime = OffsetDateTime.now().minusMinutes(10);
        StackoverflowLinkInfo linkInfo = new StackoverflowLinkInfo(link, updateTime, 1);
        StackoverflowLinkInfo oldLinkInfo = (StackoverflowLinkInfo) linkService.updateStackoverflowLink(linkInfo);

        assertEquals(link, oldLinkInfo.getLink());

        oldLinkInfo = (StackoverflowLinkInfo) linkService.updateStackoverflowLink(linkInfo);

        assertEquals(link, oldLinkInfo.getLink());
        assertEquals(updateTime, oldLinkInfo.getUpdateTime());
        assertEquals(1, oldLinkInfo.getAnswersCount());
    }
}
