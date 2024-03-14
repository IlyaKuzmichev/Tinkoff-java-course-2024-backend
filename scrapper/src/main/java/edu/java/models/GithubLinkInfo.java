package edu.java.models;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubLinkInfo extends LinkInfo {
    private Integer pullRequestsCount;
    private OffsetDateTime updateTime;
    private OffsetDateTime pushTime;

    public GithubLinkInfo(Link link, OffsetDateTime updateTime, OffsetDateTime pushTime, Integer pullRequestsCount) {
        super(link);
        this.updateTime = updateTime;
        this.pushTime = pushTime;
        this.pullRequestsCount = pullRequestsCount;
    }
}
