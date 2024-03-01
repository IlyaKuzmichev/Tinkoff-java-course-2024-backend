package edu.java.clients;


import edu.java.clients.github.GitHubClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:test", properties = "client.github.base-url=https://api.github.com")
@EnabledIfEnvironmentVariable(named = "SCRAPPER_REAL_TESTS_ENABLE", matches = "1")
public class GitHubClientRealRequestTest {
    @Autowired
    private GitHubClient gitHubClient;

    @Test
    public void testFetchRepositoryGitHubClient() {
        String repositoryName = "spring-framework";
        String ownerName = "spring-projects";

        var response = gitHubClient.fetchRepository(ownerName, repositoryName).block();

        assertNotNull(response);
        assertEquals(response.fullName(), "spring-projects/spring-framework");
        assertNotNull(response.updatedAt());
    }

    @Test
    public void testFetchRepositoryWithBadRequest() {
        String repositoryName = "abobus";
        String ownerName = "aboba";

        assertThrows(WebClientResponseException.class, () -> gitHubClient.fetchRepository(ownerName, repositoryName).block());
    }
}
