package edu.java.configuration;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqUserService;
import edu.java.service.update_checker.UpdateChecker;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {
    @Bean
    public UserService userService(
        JooqUserRepository userRepository
    ) {
        return new JooqUserService(userRepository);
    }

    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqUserRepository userRepository,
        List<UpdateChecker> updateCheckerList
    ) {
        return new JooqLinkService(linkRepository, userRepository, updateCheckerList);
    }

}
