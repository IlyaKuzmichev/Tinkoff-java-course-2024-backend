package edu.java.service.jdbc;

import edu.java.domain.links.JdbcLinkRepository;
import edu.java.models.GithubLinkInfo;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.service.LinkUpdater;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcLinkUpdater(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Optional<String> update(LinkInfo linkInfo) {
        LinkInfo oldInfo = linkRepository.updateLink(linkInfo);

        String ans = null;
        if (linkInfo.getUpdateTime().isPresent() && oldInfo.getUpdateTime().isPresent() && linkInfo.getUpdateTime().get().isAfter(oldInfo.getUpdateTime()
            .get())) {
            ans = "Link updated";

            if (linkInfo instanceof GithubLinkInfo && oldInfo instanceof GithubLinkInfo) {
                boolean hasNewPullRequests = !Objects.equals(((GithubLinkInfo) linkInfo).getPullRequestsCount(), ((GithubLinkInfo) oldInfo).getPullRequestsCount());
                if (hasNewPullRequests) {
                    ans += ": has new pull requests";
                }
            } else if (linkInfo instanceof StackoverflowLinkInfo && oldInfo instanceof StackoverflowLinkInfo) {
                boolean hasNewAnswers = !Objects.equals(((StackoverflowLinkInfo) linkInfo).getAnswersCount(), ((StackoverflowLinkInfo) oldInfo).getAnswersCount());
                if (hasNewAnswers) {
                    ans += ": has new answers";
                }
            }
        }

        return Optional.ofNullable(ans);
    }
}
