package edu.java.service.update_manager;

import edu.java.clients.bot.dto.LinkUpdateRequest;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.update_checker.StackoverflowUpdateChecker;
import edu.java.service.update_sender.LinkUpdateSenderService;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackoverflowUpdateManager implements UpdateManager {
    private final StackoverflowUpdateChecker stackoverflowUpdateChecker;
    private final LinkService linkService;
    private final UserService userService;
    private final LinkUpdateSenderService linkUpdateSenderService;

    @Override
    public void execute(Collection<Link> links) {
        for (Link link : links) {
            if (stackoverflowUpdateChecker.isAppropriateLink(link)) {
                StackoverflowLinkInfo linkInfo = stackoverflowUpdateChecker.checkUpdates(link);
                StackoverflowLinkInfo oldInfo =
                    (StackoverflowLinkInfo) linkService.updateStackoverflowLink(linkInfo);
                Optional<String> answer = prepareResponseMessage(linkInfo, oldInfo);
                answer.ifPresent(s -> linkUpdateSenderService.sendUpdate(new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    s,
                    userService.getUsersTrackLink(link))
                ));
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
