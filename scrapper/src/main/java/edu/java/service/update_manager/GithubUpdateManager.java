package edu.java.service.update_manager;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.update_checker.GithubUpdateChecker;
import edu.java.service.update_sender.LinkUpdateSenderService;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubUpdateManager implements UpdateManager {
    private final GithubUpdateChecker gitHubUpdateChecker;
    private final LinkService linkService;
    private final UserService userService;
    private final LinkUpdateSenderService linkUpdateSenderService;


    @Override
    public void execute(Collection<Link> links) {
        for (Link link : links) {
            if (gitHubUpdateChecker.isAppropriateLink(link)) {
                try {
                    GithubLinkInfo linkInfo = gitHubUpdateChecker.checkUpdates(link);
                    GithubLinkInfo oldInfo = (GithubLinkInfo) linkService.updateGithubLink(linkInfo);
                    Optional<String> answer = prepareResponseMessage(linkInfo, oldInfo);
                    answer.ifPresent(s -> linkUpdateSenderService.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        s,
                        userService.getUsersTrackLink(link))
                    ));
                } catch (RuntimeException e) {
                    log.debug(e.getMessage());
                }
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
