package edu.java.service.update_checker;

import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO Write tests
@Service
public class StackoverflowUpdateChecker implements UpdateChecker {
    private static final String HOST = "stackoverflow.com";
    private static final String TYPE = "stackoverflow";
    private final StackOverflowClient stackOverflowClient;

    @Autowired
    public StackoverflowUpdateChecker(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public StackoverflowLinkInfo checkUpdates(Link link) {
        Optional<Integer> questionId = Arrays.stream(link
                .getUrl()
                .getPath()
                .split("/"))
            .skip(2)
            .findFirst()
            .map(Integer::parseUnsignedInt);
        try {
            var response = stackOverflowClient.fetchQuestion(questionId.get()).block().get();
            return new StackoverflowLinkInfo(link, response.getLastActivityDate(), response.answersCount());
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
