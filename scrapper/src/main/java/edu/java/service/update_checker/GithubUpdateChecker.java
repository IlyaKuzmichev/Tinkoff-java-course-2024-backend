package edu.java.service.update_checker;

import edu.java.clients.github.GitHubClient;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO Write tests
@Service
public class GithubUpdateChecker implements UpdateChecker {
    private static final String HOST = "github.com";
    private static final String TYPE = "github";
    private final GitHubClient gitHubClient;

    @Autowired
    public GithubUpdateChecker(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    public GithubLinkInfo checkUpdates(Link link) {
        String[] segments = link.getUrl().getPath().split("/");
        try {
            String ownerName = segments[1];
            String repositoryName = segments[2];
            var response = gitHubClient.fetchRepository(ownerName, repositoryName).block();
            var pullRequestsResponse = gitHubClient.fetchPullRequests(ownerName, repositoryName).block();
            return new GithubLinkInfo(
                link,
                Objects.requireNonNull(response).updatedAt(),
                response.pushedAt(),
                Objects.requireNonNull(pullRequestsResponse).pullRequestsCount()
            );
        } catch (RuntimeException e) {
            throw new IncorrectRequestParametersException("Error with link check, try again");
        }
    }

    @Override
    public boolean isAppropriateLink(Link link) {
        return link.getUrl().getHost().equals(HOST);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
