package edu.java.domain.jdbc;

import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.LinkNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository {
    private static final String SELECT_LINK_ID = "SELECT id FROM links WHERE url = ?";
    private static final String GITHUB = "github";
    private static final String STACK_OVERFLOW = "stackoverflow";
    private static final String LINK_NOT_FOUND = "User %d don't track link %s";
    private static final String SQL_UPDATE_LINK_TIME_NOW =
        "UPDATE links SET last_check = NOW()::timestamp WHERE id = ?";
    private static final String LAST_UPDATE = "last_update";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addLink(Long userId, LinkInfo linkInfo, String linkType) {
        Long linkId = insertLinkIntoTables(linkInfo, linkType);

        if (isLinkNotTrackedByUser(userId, linkId)) {
            addUserTrackedLink(userId, linkId);
        } else {
            throw new AttemptAddLinkOneMoreTimeException(
                "User with ID " + userId + " already tracking link " + linkInfo.getLink().getUrl());
        }
        linkInfo.getLink().setId(linkId);
    }

    private Long insertLinkIntoTables(LinkInfo linkInfo, String linkType) {
        Long linkId;

        linkId = jdbcTemplate.queryForObject(
            "INSERT INTO links(url, link_type, last_check) VALUES (?, ?::link_type_enum, NOW()::timestamp)"
                + "ON CONFLICT (url) DO UPDATE SET link_type = EXCLUDED.link_type RETURNING id",
            Long.class,
            linkInfo.getLink().getUrl().toString().toLowerCase(),
            linkType
        );
        switch (linkType) {
            case GITHUB -> addGithubLinkToSpecificTable(linkId, linkInfo);
            case STACK_OVERFLOW -> addStackoverflowLinkToSpecificTable(linkId, linkInfo);
            default -> throw new RuntimeException("Unexpected link type");
        }
        return linkId;
    }


    @Transactional
    public Link removeLinkByURL(Long userId, URI url) {

        Long linkId = getLinkIdByUrl(url.toString().toLowerCase());
        if (linkId == null) {
            throw new LinkNotFoundException(LINK_NOT_FOUND.formatted(userId, url));
        }
        removeUserTrackedLink(userId, linkId);
        if (!isLinkTracked(linkId)) {
            removeLinkById(linkId);
        }
        return new Link(linkId, url);
    }

    public List<Link> findAllLinks() {
        String sql = "SELECT id, url FROM links";
        return jdbcTemplate.query(sql, new LinkMapper());
    }

    public List<Link> findAllLinksForUser(Long userId) {
        String sql = "SELECT id, url FROM links INNER JOIN"
            + " user_tracked_links ON id = link_id WHERE user_id = ?";
        return jdbcTemplate.query(sql, new LinkMapper(), userId);
    }

    public List<Link> findAllLinksWithCheckIntervalInSeconds(Long interval) {
        String sql = "SELECT id, url FROM links "
            + "WHERE last_check IS NULL OR EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_check)) > ?";
        return jdbcTemplate.query(sql, new LinkMapper(), interval);
    }

    @Transactional
    public LinkInfo updateGithubLink(GithubLinkInfo linkInfo) {
        Link link = linkInfo.getLink();
        jdbcTemplate.update(SQL_UPDATE_LINK_TIME_NOW, link.getId());

        String getSql = "SELECT last_update, last_push, pull_requests_count "
            + "FROM github_links WHERE link_id = ? FOR UPDATE";
        LinkInfo oldInfo = jdbcTemplate.queryForObject(getSql, (rs, rowNum) -> {
            OffsetDateTime lastUpdate = rs.getObject(LAST_UPDATE, OffsetDateTime.class);
            OffsetDateTime lastPush = rs.getObject("last_push", OffsetDateTime.class);
            Integer pullRequestsCount = rs.getInt("pull_requests_count");
            return new GithubLinkInfo(link, lastUpdate, lastPush, pullRequestsCount);
        }, link.getId());

        String setSql = "UPDATE github_links "
            + "SET last_update = ?, last_push = ?, pull_requests_count = ? WHERE link_id = ?";
        jdbcTemplate.update(
            setSql,
            linkInfo.getUpdateTime(),
            linkInfo.getPushTime(),
            linkInfo.getPullRequestsCount(),
            link.getId()
        );

        return oldInfo;
    }

    @Transactional
    public LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo) {
        Link link = linkInfo.getLink();
        jdbcTemplate.update(SQL_UPDATE_LINK_TIME_NOW, link.getId());

        String getSql = "SELECT last_update, answers_count FROM stackoverflow_links WHERE link_id = ? FOR UPDATE";
        LinkInfo oldInfo = jdbcTemplate.queryForObject(getSql, (rs, rowNum) -> {
            OffsetDateTime lastUpdate = rs.getObject(LAST_UPDATE, OffsetDateTime.class);
            Integer answersCount = rs.getInt("answers_count");
            return new StackoverflowLinkInfo(link, lastUpdate, answersCount);
        }, link.getId());

        String setSql = "UPDATE stackoverflow_links SET last_update = ?, answers_count = ? WHERE link_id = ?";
        jdbcTemplate.update(
            setSql,
            linkInfo.getUpdateTime(),
            linkInfo.getAnswersCount(),
            link.getId()
        );

        return oldInfo;
    }

    private Long getLinkIdByUrl(String url) {
        try {
            return jdbcTemplate.queryForObject(SELECT_LINK_ID, Long.class, url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void addGithubLinkToSpecificTable(Long linkId, LinkInfo linkInfo) {
        String sql = "INSERT INTO github_links (link_id, last_update, last_push, pull_requests_count)"
            + " VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(
            sql,
            linkId,
            ((GithubLinkInfo) linkInfo).getUpdateTime(),
            ((GithubLinkInfo) linkInfo).getPushTime(),
            ((GithubLinkInfo) linkInfo).getPullRequestsCount()
        );
    }

    private void addStackoverflowLinkToSpecificTable(Long linkId, LinkInfo linkInfo) {
        String sql = "INSERT INTO stackoverflow_links (link_id, last_update, answers_count) "
            + "VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(
            sql,
            linkId,
            ((StackoverflowLinkInfo) linkInfo).getUpdateTime(),
            ((StackoverflowLinkInfo) linkInfo).getAnswersCount()
        );
    }

    private boolean isLinkNotTrackedByUser(long userId, long linkId) {
        String sql = "SELECT COUNT(*) FROM user_tracked_links WHERE user_id = ? AND link_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, linkId);
        return count == null || count == 0;
    }

    private boolean isLinkTracked(long linkId) {
        String sql = "SELECT COUNT(*) FROM user_tracked_links WHERE link_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, linkId);
        return count != null && count > 0;
    }

    private void addUserTrackedLink(long userId, long linkId) {
        String sql = "INSERT INTO user_tracked_links (user_id, link_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, linkId);
    }

    private void removeUserTrackedLink(long userId, long linkId) {
        String sql = "DELETE FROM user_tracked_links WHERE user_id = ? AND link_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId, linkId);
        if (rowsAffected == 0) {
            throw new LinkNotFoundException("You don't track this link!!");
        }
    }

    private void removeLinkById(long linkId) {
        String deleteAllLinksSql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(deleteAllLinksSql, linkId);
    }

    private static final class LinkMapper implements RowMapper<Link> {

        @Override
        public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong("id");
            URI url = URI.create(rs.getString("url"));
            return new Link(id, url);
        }
    }
}
