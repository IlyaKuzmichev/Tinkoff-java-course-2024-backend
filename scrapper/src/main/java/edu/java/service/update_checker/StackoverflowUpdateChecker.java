package edu.java.service.update_checker;

import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.models.Link;
import edu.java.service.LinkUpdater;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StackoverflowUpdateChecker implements UpdateChecker {
    private static final String HOST = "stackoverflow.com";
    private final StackOverflowClient stackOverflowClient;
    private final LinkUpdater linkUpdater;

    @Autowired
    public StackoverflowUpdateChecker(StackOverflowClient stackOverflowClient, LinkUpdater linkUpdater) {
        this.stackOverflowClient = stackOverflowClient;
        this.linkUpdater = linkUpdater;
    }

    @Override
    public Optional<String> checkUpdates(Link link) {
        Optional<Integer> questionId = Arrays.stream(link
            .getUrl()
            .getPath()
            .split("/"))
            .skip(2)
            .findFirst()
            .map(Integer::parseUnsignedInt);
        if (questionId.isEmpty()) {
            return Optional.empty();
        }
        var response =  stackOverflowClient.fetchQuestion(questionId.get()).block();
        if (Objects.requireNonNull(response).isEmpty()) {
            return Optional.empty();
        }
        return linkUpdater.update(link, response.get().getLastActivityDate());
    }

    @Override
    public boolean isAppropriateLink(Link link) {
        return link.getUrl().getHost().equals(HOST);
    }
}
