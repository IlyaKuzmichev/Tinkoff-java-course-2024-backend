package edu.java.configuration;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcUserService;
import edu.java.service.update_checker.UpdateChecker;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public UserService userService(
        JdbcUserRepository userRepository
    ) {
        return new JdbcUserService(userRepository);
    }

    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcUserRepository userRepository,
        List<UpdateChecker> updateCheckerList
    ) {
        return new JdbcLinkService(linkRepository, userRepository, updateCheckerList);
    }
}
