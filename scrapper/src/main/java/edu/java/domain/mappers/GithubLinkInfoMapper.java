package edu.java.domain.mappers;

import edu.java.domain.jpa.entities.GithubLinks;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import java.net.URI;

public final class GithubLinkInfoMapper {
    private GithubLinkInfoMapper() {}

    public static GithubLinkInfo entityToLinkInfo(GithubLinks githubEntity) {
        return new GithubLinkInfo(
            new Link(githubEntity.getId(), URI.create(githubEntity.getLink().getUrl())),
            githubEntity.getLastUpdate(),
            githubEntity.getLastPush(),
            githubEntity.getPullRequestsCount()
        );
    }

    public static void linkInfoToEntity(GithubLinkInfo linkInfo, GithubLinks githubEntity) {
        githubEntity.setLastUpdate(linkInfo.getUpdateTime());
        githubEntity.setLastPush(linkInfo.getPushTime());
        githubEntity.setPullRequestsCount(linkInfo.getPullRequestsCount());
    }
}
