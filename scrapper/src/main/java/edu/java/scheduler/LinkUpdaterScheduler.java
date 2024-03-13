package edu.java.scheduler;

import edu.java.clients.bot.BotClient;
import edu.java.models.Link;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.UserService;
import edu.java.service.update_checker.UpdateChecker;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class LinkUpdaterScheduler {
    static int counter = 1;
    private final LinkService linkService;
    private final UserService userService;
    private final BotClient botClient;
    private final List<UpdateChecker> updateCheckerList;
    private final Duration checkInterval;

    private final LinkUpdater linkUpdater;

    @Autowired
    public LinkUpdaterScheduler(LinkService linkService, UserService userService, BotClient botClient,
        List<UpdateChecker> updateCheckerList, LinkUpdater linkUpdater,
        @Value("#{@scheduler.checkInterval()}") Duration checkInterval
    ) {
        this.linkService = linkService;
        this.userService = userService;
        this.botClient = botClient;
        this.updateCheckerList = updateCheckerList;
        this.linkUpdater = linkUpdater;
        this.checkInterval = checkInterval;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.invokeInterval().toMillis()}")
    public void update() {
        log.info("Scheduled method update number: " + counter);
        counter++;

        Collection<Link> links = linkService.findLinksForUpdate(checkInterval.getSeconds());
        for (Link link : links) {
            updateLink(link);
        }
    }

    private void updateLink(Link link) {
        for (UpdateChecker checker : updateCheckerList) {
            if (checker.isAppropriateLink(link)) {
                Optional<String> result = checker.checkUpdates(link);
                result.ifPresent(updateMessage -> sendUpdatesToUsers(link, updateMessage));
                break;
            }
        }
    }

    private void sendUpdatesToUsers(Link link, String updateMessage) {
        List<Long> userIds = userService.getUsersTrackLink(link);
        botClient.sendUpdates(link.getId(), link.getUrl(), updateMessage, userIds).block();
    }
}
