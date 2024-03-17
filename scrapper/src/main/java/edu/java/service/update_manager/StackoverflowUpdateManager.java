package edu.java.service.update_manager;

import edu.java.clients.bot.BotClient;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.update_checker.StackoverflowUpdateChecker;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StackoverflowUpdateManager implements UpdateManager {
    private final StackoverflowUpdateChecker stackoverflowUpdateChecker;
    private final LinkService linkService;
    private final UserService userService;
    private final BotClient botClient;

    public StackoverflowUpdateManager(
        StackoverflowUpdateChecker stackoverflowUpdateChecker,
        LinkService linkService, UserService userService, BotClient botClient
    ) {
        this.stackoverflowUpdateChecker = stackoverflowUpdateChecker;
        this.linkService = linkService;
        this.userService = userService;
        this.botClient = botClient;
    }

    @Override
    public void execute(Collection<Link> links) {
        for (Link link : links) {
            if (stackoverflowUpdateChecker.isAppropriateLink(link)) {
                StackoverflowLinkInfo linkInfo = stackoverflowUpdateChecker.checkUpdates(link);
                StackoverflowLinkInfo oldInfo =
                    (StackoverflowLinkInfo) linkService.updateStackoverflowLink(linkInfo);
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

    private Optional<String> prepareResponseMessage(StackoverflowLinkInfo linkInfo, StackoverflowLinkInfo oldInfo) {
        StringBuilder builder = new StringBuilder();

        if (linkInfo.getAnswersCount() > oldInfo.getAnswersCount()) {
            builder.append("New answer(s) in the question\n");
        } else if (linkInfo.getUpdateTime().isAfter(oldInfo.getUpdateTime())) {
            builder.append("New update at ").append(linkInfo.getUpdateTime()).append("\n");
        }

        return builder.isEmpty() ? Optional.empty() : Optional.of(builder.toString());
    }
}
