package edu.java.domain.mappers;

import edu.java.domain.jpa.entities.StackoverflowLinks;
import edu.java.models.Link;
import edu.java.models.StackoverflowLinkInfo;
import java.net.URI;

public final class StackoverflowLinkInfoMapper {
    private StackoverflowLinkInfoMapper() {}

    public static StackoverflowLinkInfo entityToLinkInfo(StackoverflowLinks stackoverflowEntity) {
        return new StackoverflowLinkInfo(
            new Link(stackoverflowEntity.getId(), URI.create(stackoverflowEntity.getLink().getUrl())),
            stackoverflowEntity.getLastUpdate(),
            stackoverflowEntity.getAnswersCount()
        );
    }

    public static void linkInfoToEntity(StackoverflowLinkInfo linkInfo, StackoverflowLinks stackoverflowEntity) {
        stackoverflowEntity.setLastUpdate(linkInfo.getUpdateTime());
        stackoverflowEntity.setAnswersCount(linkInfo.getAnswersCount());
    }
}
