package edu.java.domain;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.LinkNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext
public class JooqLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqUserRepository userRepository;

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
        assertEquals(link.getUrl().toString().toLowerCase(), allLinks.getFirst().getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLinkAlreadyTrackingByUser() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");
        assertThrows(
            AttemptAddLinkOneMoreTimeException.class,
            () -> linkRepository.addLink(user.getUserId(), linkInfo, "github")
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testAddExistingLinkTrackedByAnotherUser() {
        User user1 = new User(1L, User.Status.TRACK_LINK);
        User user2 = new User(2L, User.Status.TRACK_LINK);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user1.getUserId(), linkInfo, "github");
        linkRepository.addLink(user2.getUserId(), linkInfo, "github");

        List<Link> links = linkRepository.findAllLinks();
        assertEquals(1, links.size());
        assertEquals(link.getUrl().toString().toLowerCase(), links.getFirst().getUrl().toString());
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
    public void testRemoveNotExistingLinkFromRepository() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        assertThrows(LinkNotFoundException.class, () -> linkRepository.removeLinkByURL(
            user.getUserId(),
            URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0")
        ));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveExistingLinkThatUserNotTracking() {
        User user = new User(1L, null);
        User trackingUser = new User(2L, null);
        userRepository.addUser(user);
        userRepository.addUser(trackingUser);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(trackingUser.getUserId(), linkInfo, "github");

        assertThrows(LinkNotFoundException.class, () -> linkRepository.removeLinkByURL(
            user.getUserId(),
            link.getUrl()
        ));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinksWithCheckInterval() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");

        List<Link> links = linkRepository.findAllLinksWithCheckIntervalInSeconds(-1L);
        assertEquals(1, links.size());
        assertEquals(link.getUrl().toString().toLowerCase(), links.getFirst().getUrl().toString());
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
        assertEquals(link1.getUrl().toString().toLowerCase(), allLinks.getFirst().getUrl().toString());
        assertEquals(link2.getUrl().toString().toLowerCase(), allLinks.get(1).getUrl().toString());
        assertEquals(link3.getUrl().toString().toLowerCase(), allLinks.get(2).getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinks() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");

        List<Link> links = linkRepository.findAllLinks();
        assertEquals(1, links.size());
        assertEquals(link.getUrl().toString().toLowerCase(), links.getFirst().getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGithubLink() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        OffsetDateTime initialTime = OffsetDateTime.now().minusHours(2);
        LinkInfo linkInfo = new GithubLinkInfo(link, initialTime, initialTime, 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");

        OffsetDateTime timeUpdater = OffsetDateTime.now().minusHours(1);

        GithubLinkInfo newLinkInfo = new GithubLinkInfo(link, timeUpdater, timeUpdater, 2);
        GithubLinkInfo oldLinkInfo = (GithubLinkInfo) linkRepository.updateGithubLink(newLinkInfo);
        assertEquals(
            initialTime.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo.getUpdateTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(
            initialTime.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo.getPushTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(1, oldLinkInfo.getPullRequestsCount());

        GithubLinkInfo oldLinkInfo2 = (GithubLinkInfo) linkRepository.updateGithubLink(newLinkInfo);
        assertEquals(
            timeUpdater.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo2.getUpdateTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(
            timeUpdater.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo2.getPushTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(2, oldLinkInfo2.getPullRequestsCount());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateStackoverflowLink() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create(
            "https://stackoverflow.com/questions/78179944/pointer-type-vs-value-type-in-golang"));
        OffsetDateTime initialTime = OffsetDateTime.now().minusHours(2);
        LinkInfo linkInfo = new StackoverflowLinkInfo(link, initialTime, 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "stackoverflow");

        OffsetDateTime timeUpdater = OffsetDateTime.now().minusHours(1);

        StackoverflowLinkInfo newLinkInfo = new StackoverflowLinkInfo(link, timeUpdater, 2);
        StackoverflowLinkInfo oldLinkInfo = (StackoverflowLinkInfo) linkRepository.updateStackoverflowLink(newLinkInfo);
        assertEquals(
            initialTime.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo.getUpdateTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(1, oldLinkInfo.getAnswersCount());

        StackoverflowLinkInfo oldLinkInfo2 =
            (StackoverflowLinkInfo) linkRepository.updateStackoverflowLink(newLinkInfo);
        assertEquals(
            timeUpdater.truncatedTo(ChronoUnit.SECONDS),
            oldLinkInfo2.getUpdateTime().truncatedTo(ChronoUnit.SECONDS)
        );
        assertEquals(2, oldLinkInfo2.getAnswersCount());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindUsersTrackLinkFromUserRepository() {
        User user = new User(1L, null);
        userRepository.addUser(user);

        Link link = new Link(null, URI.create("https://github.com/IlyaKuzmichev/YandexAlgorithms-4.0"));
        LinkInfo linkInfo = new GithubLinkInfo(link, OffsetDateTime.now(), OffsetDateTime.now(), 1);
        linkRepository.addLink(user.getUserId(), linkInfo, "github");

        List<Long> users = userRepository.findUsersTrackLink(link);
        assertEquals(1, users.size());
        assertEquals(user.getUserId(), users.getFirst());
    }
}
