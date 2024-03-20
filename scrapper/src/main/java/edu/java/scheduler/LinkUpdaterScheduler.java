package edu.java.scheduler;

import edu.java.models.Link;
import edu.java.service.LinkService;
import edu.java.service.update_manager.UpdateManager;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class LinkUpdaterScheduler {
    private final Duration checkInterval;
    private final LinkService linkService;
    private final List<UpdateManager> updateManagerList;

    @Autowired
    public LinkUpdaterScheduler(
        @Value("#{@scheduler.checkInterval()}") Duration checkInterval,
        LinkService linkService,
        List<UpdateManager> updateManagerList
    ) {
        this.checkInterval = checkInterval;
        this.linkService = linkService;
        this.updateManagerList = updateManagerList;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.invokeInterval().toMillis()}")
    public void update() {
        Collection<Link> links = linkService.findLinksForUpdate(checkInterval.getSeconds());
        for (UpdateManager updateManager : updateManagerList) {
            updateManager.execute(links);
        }
    }

}
