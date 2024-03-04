package edu.java.domain.links;

import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.controller.exception.ChatIdNotFoundException;
import edu.java.controller.exception.IncorrectRequestParametersException;
import edu.java.controller.exception.LinkNotFoundException;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private static final String USER_NOT_REGISTERED = "User with chat ID %d not registered";
    private static final String SELECT_LINK_ID = "SELECT link_id FROM all_links WHERE url = ?";
    private static final String GITHUB = "github";
    private static final String STACK_OVERFLOW = "stackoverflow";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void addLink(Long chatId, URI url) {
        Long userId = getUserId(chatId);
        if (userId == null) {
            throw new ChatIdNotFoundException(USER_NOT_REGISTERED.formatted(chatId));
        }

        Long linkId = getLinkIdByUrl(url.toString());
        String linkType = parseLinkType(url);
        if (linkType == null) {
            throw new IncorrectRequestParametersException("Invalid link for scrapper tracking");
        }

        if (linkId == null) {
            linkId = addLinkToAllLinks(url.toString(), linkType);

            if (linkType.equals(GITHUB)) {
                addGithubLink(linkId);
            } else if (linkType.equals(STACK_OVERFLOW)) {
                addStackOverflowLink(linkId);
            }
        }

        if (isLinkNotTrackedByUser(userId, linkId)) {
            addUserTrackedLink(userId, linkId);
        } else {
            throw new AttemptAddLinkOneMoreTimeException(
                "User with chat ID " + chatId + "already tracking link " + url);
        }
    }

    @Override
    @Transactional
    public void removeLink(Long chatId, URI url) {
        Long userId = getUserId(chatId);
        if (userId == null) {
            throw new ChatIdNotFoundException(USER_NOT_REGISTERED.formatted(chatId));
        }

        Long linkId = getLinkIdByUrl(url.toString());
        if (linkId == null || isLinkNotTrackedByUser(userId, linkId)) {
            throw new LinkNotFoundException("Link with URL " + url + "isn't tracking by user with chat ID " + chatId);
        }

        removeUserTrackedLink(userId, linkId);
        if (!isLinkTracked(linkId)) {
            removeLinkById(linkId);
        }
    }

    @Override
    @Transactional
    public ListLinksResponse findAllLinks() {
        String sql = "SELECT link_id, url FROM all_links";
        List<LinkResponse> linkResponses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("link_id");
            String url = rs.getString("url");
            return new LinkResponse(id, URI.create(url));
        });

        int size = linkResponses.size();
        return new ListLinksResponse(linkResponses, size);
    }

    private Long getUserId(long chatId) {
        String sql = "SELECT user_id FROM users WHERE chat_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, chatId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Long getLinkIdByUrl(String url) {
        try {
            return jdbcTemplate.queryForObject(SELECT_LINK_ID, Long.class, url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Long addLinkToAllLinks(String url, String linkType) {
        String sql = "INSERT INTO all_links (url, link_type) VALUES (?, ?)";
        jdbcTemplate.update(sql, url, linkType);
        return jdbcTemplate.queryForObject(SELECT_LINK_ID, Long.class, url);
    }

    private void addGithubLink(long linkId) {
        String sql = "INSERT INTO github_links (link_id) VALUES (?)";
        jdbcTemplate.update(sql, linkId);
    }

    private void addStackOverflowLink(long linkId) {
        String sql = "INSERT INTO stackoverflow_links (link_id) VALUES (?)";
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

        String deleteAllLinksSql = "DELETE FROM all_links WHERE link_id = ?";
        jdbcTemplate.update(deleteAllLinksSql, linkId);
    }

    private String parseLinkType(URI url) {
        String link = url.toString();

        if (link.contains("https://github.com/")) {
            return GITHUB;
        }
        if (link.contains("https://stackoverflow.com/")) {
            return STACK_OVERFLOW;
        }
        return null;
    }
}
