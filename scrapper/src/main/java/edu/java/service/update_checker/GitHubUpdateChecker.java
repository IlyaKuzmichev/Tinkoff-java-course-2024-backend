package edu.java.service.update_checker;

import edu.java.clients.github.GitHubClient;
import edu.java.models.Link;
import edu.java.service.LinkUpdater;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubUpdateChecker implements UpdateChecker {
    private static final String HOST = "github.com";
    private final GitHubClient gitHubClient;
    private final LinkUpdater linkUpdater;

    @Autowired
    public GitHubUpdateChecker(GitHubClient gitHubClient, LinkUpdater linkUpdater) {
        this.gitHubClient = gitHubClient;
        this.linkUpdater = linkUpdater;
    }

    @Override
    public Optional<String> checkUpdates(Link link) {
        String[] segments = link.getUrl().getPath().split("/");
        String ownerName = segments[1];
        String repositoryName = segments[2];
        var response = gitHubClient.fetchRepository(ownerName, repositoryName);
        return linkUpdater.update(link, Objects.requireNonNull(response.block()).updatedAt());
    }

    @Override
    public boolean isCommonChecker(Link link) {
        return link.getUrl().getHost().equals(HOST);
    }
}
