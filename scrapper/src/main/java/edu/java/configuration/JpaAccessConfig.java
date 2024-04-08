package edu.java.configuration;

import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackoverflowLinkRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.JpaUserTrackedLinkRepository;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaUserService;
import edu.java.service.update_checker.UpdateChecker;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    @Bean
    public UserService userService(
        JpaUserRepository userRepository,
        JpaUserTrackedLinkRepository userTrackedLinkRepository
    ) {
        return new JpaUserService(
            userRepository,
            userTrackedLinkRepository
        );
    }

    @Bean
    public LinkService linkService(
        JpaUserRepository userRepository,
        JpaLinkRepository linkRepository,
        JpaGithubLinkRepository githubLinkRepository,
        JpaStackoverflowLinkRepository stackoverflowLinkRepository,
        JpaUserTrackedLinkRepository userTrackedLinkRepository,
        List<UpdateChecker> updateCheckers
    ) {
        return new JpaLinkService(
            userRepository,
            linkRepository,
            githubLinkRepository,
            stackoverflowLinkRepository,
            userTrackedLinkRepository,
            updateCheckers
        );
    }

}


