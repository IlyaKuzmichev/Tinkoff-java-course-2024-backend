package edu.java.domain.links;

import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.exception.LinkNotFoundException;
import edu.java.models.Link;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addLink(Long userId, Link link) {
        Long linkId = insertLinkIntoTables(link.getUrl());

        if (isLinkNotTrackedByUser(userId, linkId)) {
            addUserTrackedLink(userId, linkId);
        } else {
            throw new AttemptAddLinkOneMoreTimeException(
                "User with ID " + userId + "already tracking link " + link.getUrl());
        }
        link.setId(linkId);
    }

    private Long insertLinkIntoTables(URI url) {
        String linkType = parseLinkType(url);
        if (linkType == null) {
            throw new IncorrectRequestParametersException("Invalid link for scrapper tracking");
        }
        Long linkId;

        linkId = jdbcTemplate.queryForObject(
            "INSERT INTO links(url, link_type) VALUES (?, ?::link_type_enum) "
                + "ON CONFLICT (url) DO UPDATE SET link_type = EXCLUDED.link_type RETURNING id",
            Long.class,
            url.toString(),
            linkType
        );
        addLinkToCommonTable(linkId, linkType);

        return linkId;
    }


    @Transactional
    public Link removeLinkByURL(Long userId, URI url) {

        Long linkId = getLinkIdByUrl(url.toString());
        if (linkId == null) {
            throw new LinkNotFoundException(LINK_NOT_FOUND.formatted(userId, url));
        }
        removeUserTrackedLink(userId, linkId);
        if (!isLinkTracked(linkId)) {
            removeLinkById(linkId);
        }
        return new Link(linkId, url);
    }


    @Transactional
    public List<Link> findAllLinks() {
        String sql = "SELECT id, url FROM links";
        return jdbcTemplate.query(sql, new LinkMapper());
    }

    @Transactional
    public List<Link> findAllLinksForUser(Long userId) {
        String sql = "SELECT id, url FROM links INNER JOIN"
            + " user_tracked_links ON id = link_id WHERE user_id = ?";
        return jdbcTemplate.query(sql, new LinkMapper(), userId);
    }

    @Transactional
    public List<Link> findAllLinksWithCheckInterval(Long interval) {
        String sql = "SELECT id, url FROM links "
            + "WHERE last_check IS NULL OR EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_check)) > ?";
        return jdbcTemplate.query(sql, new LinkMapper(), interval);
    }

    @Transactional
    public boolean updateLink(Link link, OffsetDateTime updateTime) {
        String sql = "UPDATE links SET last_check = NOW()::timestamp WHERE id = ?";
        jdbcTemplate.update(sql, link.getId());
        String linkType = parseLinkType(link.getUrl());

        String getSql = "SELECT last_update FROM %s_links WHERE link_id = ? FOR UPDATE".formatted(linkType);
        Optional<OffsetDateTime> lastUpdate = Optional.ofNullable(
            jdbcTemplate.queryForObject(getSql, OffsetDateTime.class, link.getId()));

        String setSql = "UPDATE %s_links SET last_update = ? WHERE link_id = ?".formatted(linkType);
        jdbcTemplate.update(setSql, updateTime, link.getId());
        return lastUpdate.isPresent() && updateTime.isAfter(lastUpdate.get());
    }

    private Long getLinkIdByUrl(String url) {
        try {
            return jdbcTemplate.queryForObject(SELECT_LINK_ID, Long.class, url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void addLinkToCommonTable(long linkId, String linkType) {
        String sql = "INSERT INTO %s_links (link_id) VALUES (?) ON CONFLICT DO NOTHING".formatted(linkType);
        jdbcTemplate.update(sql, linkId);
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
        jdbcTemplate.update(sql, userId, linkId);
    }

    private void removeLinkById(long linkId) {
        String deleteGithubLinkSql = "DELETE FROM github_links WHERE link_id = ?";
        jdbcTemplate.update(deleteGithubLinkSql, linkId);

        String deleteStackOverflowLinkSql = "DELETE FROM stackoverflow_links WHERE link_id = ?";
        jdbcTemplate.update(deleteStackOverflowLinkSql, linkId);

        String deleteAllLinksSql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(deleteAllLinksSql, linkId);
    }

    private static String parseLinkType(URI url) {
        String link = url.toString();

        if (link.contains("https://github.com/")) {
            return GITHUB;
        }
        if (link.contains("https://stackoverflow.com/")) {
            return STACK_OVERFLOW;
        }
        return null;
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
