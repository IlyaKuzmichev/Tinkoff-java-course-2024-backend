package edu.java.service.jdbc;

import edu.java.domain.links.JdbcLinkRepository;
import edu.java.domain.users.JdbcUserRepository;
import edu.java.exception.IncorrectUserStatusException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcUserRepository userRepository;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository linkRepository, JdbcUserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Link addLink(Long chatId, URI url) {
        User user = checkUser(chatId, User.Status.TRACK_LINK);
        user.setStatus(User.Status.BASE);
        userRepository.updateUser(user);

        Link link = new Link(null, url);
        linkRepository.addLink(chatId, link);
        return link;
    }

    @Override
    @Transactional
    public Link removeLinkByURL(Long chatId, URI url) {
        User user = checkUser(chatId, User.Status.UNTRACK_LINK);
        user.setStatus(User.Status.BASE);
        userRepository.updateUser(user);

        return linkRepository.removeLinkByURL(chatId, url);
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        return linkRepository.findAllLinksForUser(chatId);
    }

    @Override
    public Collection<Link> findLinksForUpdate(Long interval) {
        return linkRepository.findAllLinksWithCheckInterval(interval);
    }

    private User checkUser(Long userId, User.Status expectedStatus) {
        Optional<User> user = userRepository.findUser(userId);
        if (user.isEmpty()) {
            throw new UserIdNotFoundException(userId);
        } else if (user.get().getStatus() != expectedStatus) {
            throw new IncorrectUserStatusException(userId);
        }
        return user.get();
    }
}
