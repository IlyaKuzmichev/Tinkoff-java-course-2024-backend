package edu.java.service;

import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackoverflowLinkRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.JpaUserTrackedLinkRepository;
import edu.java.domain.jpa.entities.Users;
import edu.java.models.User;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaUserService;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@Sql(value = "classpath:sql/insert_values_to_db.sql",
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql (value = "classpath:sql/clean_db.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaUserService userService;
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

//    @Order(1)
//    public void testDatabaseBeforeTestingServices() {
//        assertEquals(3, userRepository.findAll().size());
//        assertEquals(4, linkRepository.findAll().size());
//        assertEquals(2, githubLinkRepository.findAll().size());
//        assertEquals(2, stackoverflowLinkRepository.findAll().size());
//        assertEquals(7, userTrackedLinkRepository.findAll().size());
//
//        assertEquals(1, userTrackedLinkRepository.findAllByLinkId(1L).size());
//        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(2L).size());
//        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(3L).size());
//        assertEquals(2, userTrackedLinkRepository.findAllByLinkId(4L).size());
//    }

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
}
