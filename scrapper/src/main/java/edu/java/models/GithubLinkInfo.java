package edu.java.models;

import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubLinkInfo extends LinkInfo {
    private Integer pullRequestsCount;

    public GithubLinkInfo(Link link, Optional<OffsetDateTime> updateTime, Integer pullRequestsCount) {
        super(link, updateTime);
        this.pullRequestsCount = pullRequestsCount;
    }
}
