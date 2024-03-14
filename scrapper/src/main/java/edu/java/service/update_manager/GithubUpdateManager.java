package edu.java.service.update_manager;

import edu.java.clients.bot.BotClient;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.update_checker.GithubUpdateChecker;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GithubUpdateManager implements UpdateManager {
    private final GithubUpdateChecker gitHubUpdateChecker;
    private final LinkService linkService;
    private final UserService userService;
    private final BotClient botClient;

    public GithubUpdateManager(
        GithubUpdateChecker gitHubUpdateChecker,
        LinkService linkService, UserService userService, BotClient botClient
    ) {
        this.gitHubUpdateChecker = gitHubUpdateChecker;
        this.linkService = linkService;
        this.userService = userService;
        this.botClient = botClient;
    }

    @Override
    public void execute(Collection<Link> links) {
        for (Link link : links) {
            if (gitHubUpdateChecker.isAppropriateLink(link)) {
                GithubLinkInfo linkInfo = gitHubUpdateChecker.checkUpdates(link);
                GithubLinkInfo oldInfo = (GithubLinkInfo) linkService.updateGithubLink(linkInfo);
                Optional<String> answer = prepareResponseMessage(linkInfo, oldInfo);
                answer.ifPresent(s -> botClient.sendUpdates(
                    link.getId(),
                    link.getUrl(),
                    s,
                    userService.getUsersTrackLink(link)
                ).block());
            }
        }
    }

    private Optional<String> prepareResponseMessage(GithubLinkInfo linkInfo, GithubLinkInfo oldInfo) {
        StringBuilder builder = new StringBuilder();

        if (linkInfo.getPullRequestsCount() > oldInfo.getPullRequestsCount()) {
            builder.append("Repository has new pull-request\n");
        } else if (linkInfo.getPushTime().isAfter(oldInfo.getPushTime())) {
            builder.append("New push to repository at ").append(linkInfo.getPushTime()).append("\n");
        }
        if (linkInfo.getUpdateTime().isAfter(oldInfo.getUpdateTime())) {
            builder.append("New update at ").append(linkInfo.getUpdateTime()).append("\n");
        }

        return builder.isEmpty() ? Optional.empty() : Optional.of(builder.toString());
    }
}
