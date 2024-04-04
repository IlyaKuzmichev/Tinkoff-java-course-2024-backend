package edu.java.service;

import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackoverflowLinkRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.JpaUserTrackedLinkRepository;
import edu.java.domain.jpa.entities.Users;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jpa.JpaLinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@Sql(value = "classpath:sql/insert_values_to_db.sql",
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(locations = "classpath:test")
public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaGithubLinkRepository githubLinkRepository;
    @Autowired
    private JpaStackoverflowLinkRepository stackoverflowLinkRepository;
    @Autowired
    private JpaUserTrackedLinkRepository userTrackedLinkRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void testDatabaseExistRowsFromSchema() {
        assertEquals(3, userRepository.findAll().size());
        assertEquals(4, linkRepository.findAll().size());
        assertEquals(2, githubLinkRepository.findAll().size());
        assertEquals(2, stackoverflowLinkRepository.findAll().size());
        assertEquals(7, userTrackedLinkRepository.findAll().size());

        assertEquals(1, userTrackedLinkRepository.findAllByLinkId(1L).size());
        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(2L).size());
        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(3L).size());
        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(4L).size());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByUrlThatUserTrackingNotSingleTracking() {
        Optional<Users> optUser = userRepository.findById(101L);
        assertTrue(optUser.isPresent());
        Users user = optUser.get();
        user.setUserStatus(User.Status.UNTRACK_LINK);
        userRepository.saveAndFlush(user);

        linkService.removeLinkByURL(101L, URI.create("https://github.com/IlyaKuzmichev/s21_Containers"));

        assertEquals(3, userTrackedLinkRepository.findAllByUserId(101L).size());
        assertEquals(1, userTrackedLinkRepository.findAllByLinkId(2L).size());
        assertEquals(2, githubLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByUrlThatOnlyThisUserTrack() {
        Optional<Users> optUser = userRepository.findById(101L);
        assertTrue(optUser.isPresent());
        Users user = optUser.get();
        user.setUserStatus(User.Status.UNTRACK_LINK);
        userRepository.saveAndFlush(user);

        linkService.removeLinkByURL(101L, URI.create("https://github.com/IlyaKuzmichev/Scrapper_scammer"));

        assertEquals(3, userTrackedLinkRepository.findAllByUserId(101L).size());
        assertEquals(0, userTrackedLinkRepository.findAllByLinkId(1L).size());
        assertEquals(1, githubLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkThatUserNotTracking() {
        Optional<Users> optUser = userRepository.findById(102L);
        assertTrue(optUser.isPresent());
        Users user = optUser.get();
        user.setUserStatus(User.Status.UNTRACK_LINK);
        userRepository.saveAndFlush(user);

        assertThrows(
            LinkNotFoundException.class,
            () -> linkService.removeLinkByURL(102L, URI.create("https://github.com/IlyaKuzmichev/Scrapper_scammer"))
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkByNotExistingUser() {
        assertThrows(
            UserIdNotFoundException.class,
            () -> linkService.removeLinkByURL(1984L, URI.create("https://github.com/IlyaKuzmichev/Scrapper_scammer"))
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinksForUser() {
        assertEquals(4, linkService.findAllLinksForUser(101L).size());
        assertEquals(2, linkService.findAllLinksForUser(102L).size());
        assertEquals(1, linkService.findAllLinksForUser(103L).size());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinksForNotExistingUser() {
        assertThrows(
            UserIdNotFoundException.class,
            () -> linkService.findAllLinksForUser(1984L)
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testFindLinksForUpdate() {
        assertEquals(3, linkService.findLinksForUpdate(0L).size());
        assertEquals(0, linkService.findLinksForUpdate(1234567890L).size());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGithubLink() {
        Link link = new Link(
            2L,
            URI.create("https://github.com/IlyaKuzmichev/s21_Containers")
        );
        OffsetDateTime newTime = OffsetDateTime.parse("2024-04-03T11:11:11.111111+00:00");
        OffsetDateTime oldTime = OffsetDateTime.parse("2024-02-02T11:11:11.111111+00:00");
        Integer newPullRequestCount = 10;
        Integer oldPullRequestCount = 9;
        GithubLinkInfo newLinkInfo = new GithubLinkInfo(link, newTime, newTime, newPullRequestCount);

        GithubLinkInfo oldLinkInfo = (GithubLinkInfo) linkService.updateGithubLink(newLinkInfo);

        assertEquals(oldTime, oldLinkInfo.getUpdateTime());
        assertEquals(oldTime, oldLinkInfo.getPushTime());
        assertEquals(oldPullRequestCount, oldLinkInfo.getPullRequestsCount());

        oldLinkInfo = (GithubLinkInfo) linkService.updateGithubLink(newLinkInfo);

        assertEquals(newTime, oldLinkInfo.getUpdateTime());
        assertEquals(newTime, oldLinkInfo.getPushTime());
        assertEquals(newPullRequestCount, oldLinkInfo.getPullRequestsCount());

    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateStackoverflowLink() {
        Link link = new Link(
            4L,
            URI.create("https://stackoverflow.com/questions/218384/what-is-a-nullpointerexception-and-how-do-i-fix-it")
        );
        OffsetDateTime newTime = OffsetDateTime.parse("2024-04-03T11:11:11.111111+00:00");
        OffsetDateTime oldTime = OffsetDateTime.parse("2024-01-01T11:11:11.111111+00:00");
        Integer newAnswerCount = 9;
        Integer oldAnswerCount = 8;
        StackoverflowLinkInfo newLinkInfo = new StackoverflowLinkInfo(link, newTime, newAnswerCount);

        StackoverflowLinkInfo oldLinkInfo = (StackoverflowLinkInfo) linkService.updateStackoverflowLink(newLinkInfo);

        assertEquals(oldTime, oldLinkInfo.getUpdateTime());
        assertEquals(oldAnswerCount, oldLinkInfo.getAnswersCount());

        oldLinkInfo = (StackoverflowLinkInfo) linkService.updateStackoverflowLink(newLinkInfo);

        assertEquals(newTime, oldLinkInfo.getUpdateTime());
        assertEquals(newAnswerCount, oldLinkInfo.getAnswersCount());

    }
}
