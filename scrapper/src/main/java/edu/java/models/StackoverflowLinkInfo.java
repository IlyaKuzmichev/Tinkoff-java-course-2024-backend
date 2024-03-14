package edu.java.models;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackoverflowLinkInfo extends LinkInfo {
    private Integer answersCount;
    private OffsetDateTime updateTime;

    public StackoverflowLinkInfo(Link link, OffsetDateTime updateTime, Integer answersCount) {
        super(link);
        this.updateTime = updateTime;
        this.answersCount = answersCount;
    }
}
