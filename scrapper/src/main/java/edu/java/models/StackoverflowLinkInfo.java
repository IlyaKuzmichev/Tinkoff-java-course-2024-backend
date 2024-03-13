package edu.java.models;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
@Setter
public class StackoverflowLinkInfo extends LinkInfo {
    private Integer answersCount;

    public StackoverflowLinkInfo(Link link, Optional<OffsetDateTime> updateTime, Integer answersCount) {
        super(link, updateTime);
        this.answersCount = answersCount;
    }
}
