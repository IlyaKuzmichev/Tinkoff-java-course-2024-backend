package edu.java.service.jdbc;

import edu.java.domain.links.JdbcLinkRepository;
import edu.java.models.Link;
import edu.java.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcLinkUpdater(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Optional<String> update(Link link, OffsetDateTime updateTime) {
        boolean isUpdated = linkRepository.updateLink(link, updateTime);
        String ans = isUpdated ? "Repository updated" : null;
        return Optional.ofNullable(ans);
    }
}
