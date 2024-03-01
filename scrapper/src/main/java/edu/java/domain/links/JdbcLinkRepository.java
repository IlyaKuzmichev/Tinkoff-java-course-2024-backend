package edu.java.domain.links;

import edu.java.controller.dto.ListLinksResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.net.URI;

public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLink(Long chatId, URI url) {
        Long linkId = getLinkIdByUrl(url.toString());
        String linkType = url.getHost();

        if (linkId == null) {
            linkId = addLinkToAllLinks(url.toString(), linkType);

            if (linkType.equals("github")) {
                addGithubLink(linkId);
            } else if (linkType.equals("stackoverflow")) {
                addStackOverflowLink(linkId);
            }
        }

        if (!isLinkTrackedByUser(chatId, linkId)) {
            addUserTrackedLink(chatId, linkId);
        }
    }

    @Override
    public void removeLink(Long chatId, URI url) {

    }

    @Override
    public ListLinksResponse findAllLinks() {
        return null;
    }

    private Long getLinkIdByUrl(String url) {
        String sql = "SELECT link_id FROM all_links WHERE url = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Long addLinkToAllLinks(String url, String linkType) {
        String sql = "INSERT INTO all_links (url, link_type) VALUES (?, ?)";
        String sqlReturnId = "SELECT link_id FROM all_links WHERE url = ?";
        jdbcTemplate.update(sql, url, linkType);
        return jdbcTemplate.queryForObject(sqlReturnId, Long.class, url);
    }

    private void addGithubLink(long linkId) {
        String sql = "INSERT INTO github_links (link_id) VALUES (?)";
        jdbcTemplate.update(sql, linkId);
    }

    private void addStackOverflowLink(long linkId) {
        String sql = "INSERT INTO stackoverflow_links (link_id) VALUES (?)";
        jdbcTemplate.update(sql, linkId);
    }

    private boolean isLinkTrackedByUser(long chatId, long linkId) {
        String sql = "SELECT COUNT(*) FROM user_tracked_links WHERE user_id = ? AND link_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, chatId, linkId) > 0;
    }

    private void addUserTrackedLink(long chatId, long linkId) {
        String sql = "INSERT INTO user_tracked_links (user_id, link_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, chatId, linkId);
    }
}
