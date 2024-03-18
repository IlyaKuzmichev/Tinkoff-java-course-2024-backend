package edu.java.domain.jooq;

import edu.java.domain.jooq.enums.LinkTypeEnum;
import edu.java.domain.jooq.tables.records.GithubLinksRecord;
import edu.java.domain.jooq.tables.records.LinksRecord;
import edu.java.domain.jooq.tables.records.StackoverflowLinksRecord;
import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.LinkNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.Tables.GITHUB_LINKS;
import static edu.java.domain.jooq.Tables.LINKS;
import static edu.java.domain.jooq.Tables.STACKOVERFLOW_LINKS;
import static edu.java.domain.jooq.Tables.USER_TRACKED_LINKS;

@Repository
public class JooqLinkRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Transactional
    public void addLink(Long userId, LinkInfo linkInfo, String linkType) {
        LinksRecord linksRecord = addLinkRecord(linkInfo.getLink().getUrl(), linkType);
        linkInfo.getLink().setId(linksRecord.getId());

        switch (linkType.toLowerCase()) {
            case "github":
                addGithubLinkRecord(linksRecord.getId(), ((GithubLinkInfo) linkInfo));
                break;
            case "stackoverflow":
                addStackoverflowLinkRecord(linksRecord.getId(), ((StackoverflowLinkInfo) linkInfo));
                break;
            default:
                throw new IllegalArgumentException("Invalid link type: " + linkType);
        }

        addUserTrackedLink(userId, linksRecord.getId());
    }

    @Transactional
    public Link removeLinkByURL(Long userId, URI url) {
        LinksRecord linksRecord = dslContext.selectFrom(LINKS)
            .where(LINKS.URL.eq(url.toString().toLowerCase()))
            .fetchOne();

        if (linksRecord == null) {
            throw new LinkNotFoundException(String.format("Link with URL '%s' not found", url));
        }
        Long linkId = linksRecord.getId();

        int rowsAffected = dslContext.deleteFrom(USER_TRACKED_LINKS)
            .where(USER_TRACKED_LINKS.USER_ID.eq(userId)
                .and(USER_TRACKED_LINKS.LINK_ID.eq(linkId)))
            .execute();

        if (rowsAffected == 0) {
            throw new LinkNotFoundException(String.format("Link with URL '%s' not tracked", url));
        }

        clearDBFromUntrackedLink(linkId);
        return new Link(linkId, url);
    }

    @Transactional(readOnly = true)
    public List<Link> findAllLinks() {
        return dslContext.selectFrom(LINKS)
            .fetch()
            .map(rec -> new Link(rec.getId(), URI.create(rec.getUrl())));
    }

    @Transactional(readOnly = true)
    public List<Link> findAllLinksForUser(Long userId) {
        return dslContext.select(LINKS.ID, LINKS.URL)
            .from(LINKS)
            .join(USER_TRACKED_LINKS).on(USER_TRACKED_LINKS.LINK_ID.eq(LINKS.ID))
            .where(USER_TRACKED_LINKS.USER_ID.eq(userId))
            .fetch()
            .map(rec -> new Link(rec.get(LINKS.ID), URI.create(rec.get(LINKS.URL))));
    }

    @Transactional(readOnly = true)
    public List<Link> findAllLinksWithCheckInterval(Long interval) {
        OffsetDateTime threshold = OffsetDateTime.now().minusSeconds(interval);

        Result<Record> result = dslContext.select()
            .from(LINKS)
            .where(LINKS.LAST_CHECK.isNull().or(LINKS.LAST_CHECK.lessOrEqual(threshold)))
            .fetch();

        return result.map(rec -> {
            Long id = rec.get(LINKS.ID);
            URI url = URI.create(rec.get(LINKS.URL));
            return new Link(id, url);
        });
    }

    @Transactional
    public LinkInfo updateGithubLink(GithubLinkInfo linkInfo) {
        Link link = linkInfo.getLink();

        dslContext.update(LINKS)
            .set(LINKS.LAST_CHECK, OffsetDateTime.now())
            .where(LINKS.ID.eq(link.getId()))
            .execute();

        GithubLinksRecord oldInfo = dslContext.selectFrom(GITHUB_LINKS)
            .where(GITHUB_LINKS.LINK_ID.eq(link.getId()))
            .forUpdate()
            .fetchOne();

        dslContext.update(GITHUB_LINKS)
            .set(GITHUB_LINKS.LAST_UPDATE, linkInfo.getUpdateTime())
            .set(GITHUB_LINKS.LAST_PUSH, linkInfo.getPushTime())
            .set(GITHUB_LINKS.PULL_REQUESTS_COUNT, linkInfo.getPullRequestsCount())
            .where(GITHUB_LINKS.LINK_ID.eq(link.getId()))
            .execute();

        OffsetDateTime lastUpdate = Objects.requireNonNull(oldInfo).getLastUpdate();
        OffsetDateTime lastPush = oldInfo.getLastPush();
        Integer pullRequestsCount = oldInfo.getPullRequestsCount();
        return new GithubLinkInfo(link, lastUpdate, lastPush, pullRequestsCount);
    }

    @Transactional
    public LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo) {
        Link link = linkInfo.getLink();

        dslContext.update(LINKS)
            .set(LINKS.LAST_CHECK, OffsetDateTime.now())
            .where(LINKS.ID.eq(link.getId()))
            .execute();

        StackoverflowLinksRecord oldInfo = dslContext.selectFrom(STACKOVERFLOW_LINKS)
            .where(STACKOVERFLOW_LINKS.LINK_ID.eq(link.getId()))
            .forUpdate()
            .fetchOne();

        dslContext.update(STACKOVERFLOW_LINKS)
            .set(STACKOVERFLOW_LINKS.LAST_UPDATE, linkInfo.getUpdateTime())
            .set(STACKOVERFLOW_LINKS.ANSWERS_COUNT, linkInfo.getAnswersCount())
            .where(STACKOVERFLOW_LINKS.LINK_ID.eq(link.getId()))
            .execute();

        OffsetDateTime lastUpdate = Objects.requireNonNull(oldInfo).getLastUpdate();
        Integer answersCount = oldInfo.getAnswersCount();
        return new StackoverflowLinkInfo(link, lastUpdate, answersCount);
    }

    private LinksRecord addLinkRecord(URI url, String linkType) {
        return dslContext.insertInto(LINKS)
            .set(LINKS.URL, url.toString().toLowerCase())
            .set(LINKS.LINK_TYPE, LinkTypeEnum.lookupLiteral(linkType))
            .set(LINKS.LAST_CHECK, OffsetDateTime.now())
            .onConflict(LINKS.URL)
            .doUpdate()
            .set(LINKS.LINK_TYPE, LINKS.LINK_TYPE)
            .returning()
            .fetchOne();
    }

    private void addGithubLinkRecord(Long linkId, GithubLinkInfo linkInfo) {
        dslContext.insertInto(GITHUB_LINKS)
            .set(GITHUB_LINKS.LINK_ID, linkId)
            .set(GITHUB_LINKS.LAST_UPDATE, linkInfo.getUpdateTime())
            .set(GITHUB_LINKS.LAST_PUSH, linkInfo.getPushTime())
            .set(GITHUB_LINKS.PULL_REQUESTS_COUNT, linkInfo.getPullRequestsCount())
            .onDuplicateKeyIgnore()
            .execute();
    }

    private void addStackoverflowLinkRecord(Long linkId, StackoverflowLinkInfo linkInfo) {
        dslContext.insertInto(STACKOVERFLOW_LINKS)
            .set(STACKOVERFLOW_LINKS.LINK_ID, linkId)
            .set(STACKOVERFLOW_LINKS.LAST_UPDATE, linkInfo.getUpdateTime())
            .set(STACKOVERFLOW_LINKS.ANSWERS_COUNT, linkInfo.getAnswersCount())
            .onDuplicateKeyIgnore()
            .execute();
    }

    private void addUserTrackedLink(Long userId, Long linkId) {
        try {
            dslContext.insertInto(USER_TRACKED_LINKS)
                .set(USER_TRACKED_LINKS.USER_ID, userId)
                .set(USER_TRACKED_LINKS.LINK_ID, linkId)
                .execute();
        } catch (DuplicateKeyException e) {
            throw new AttemptAddLinkOneMoreTimeException("You already tracking this link");
        }
    }

    private boolean isLinkTracked(Long linkId) {
        return dslContext.fetchExists(USER_TRACKED_LINKS, USER_TRACKED_LINKS.LINK_ID.eq(linkId));
    }

    private void clearDBFromUntrackedLink(Long linkId) {
        if (!isLinkTracked(linkId)) {
//            dslContext.deleteFrom(GITHUB_LINKS)
//                .where(GITHUB_LINKS.LINK_ID.eq(linkId))
//                .execute();
//            dslContext.deleteFrom(STACKOVERFLOW_LINKS)
//                .where(STACKOVERFLOW_LINKS.LINK_ID.eq(linkId))
//                .execute();
            dslContext.deleteFrom(LINKS)
                .where(LINKS.ID.eq(linkId))
                .execute();
        }
    }
}
