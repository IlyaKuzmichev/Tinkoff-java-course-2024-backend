package edu.java.service.jdbc;

import edu.java.domain.links.JdbcLinkRepository;
import edu.java.domain.users.JdbcUserRepository;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.exception.IncorrectUserStatusException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.Link;
import edu.java.models.User;
import edu.java.service.LinkService;
import edu.java.service.update_checker.UpdateChecker;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcUserRepository userRepository;
    private final List<UpdateChecker> updateCheckerList;

    @Autowired
    public JdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcUserRepository userRepository,
        List<UpdateChecker> updateCheckerList
    ) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.updateCheckerList = updateCheckerList;
    }

    @Override
    @Transactional
    public Link addLink(Long chatId, URI url) {
        updateLinkChangingUser(chatId, User.Status.TRACK_LINK);

        Link link = new Link(null, url);

        for (UpdateChecker checker : updateCheckerList) {
            if (checker.isAppropriateLink(link)) {
                try {
                    checker.checkUpdates(link);
                } catch (RuntimeException e) {
                    throw new IncorrectRequestParametersException("Incorrect link, please try again");
                }
            }
        }

        linkRepository.addLink(chatId, link);
        return link;
    }

    @Override
    @Transactional
    public Link removeLinkByURL(Long chatId, URI url) {
        updateLinkChangingUser(chatId, User.Status.UNTRACK_LINK);
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

    @Transactional
    protected void updateLinkChangingUser(Long chatId, User.Status expectedStatus) {
        User user = findUser(chatId);
        User.Status prevStatus = user.getStatus();
        resetUserStatusToBase(user);
        if (prevStatus != expectedStatus) {
            throw new IncorrectUserStatusException(chatId);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    protected void resetUserStatusToBase(User user) {
        user.setStatus(User.Status.BASE);
        userRepository.updateUser(user);
    }

    private User findUser(Long userId) {
        Optional<User> user = userRepository.findUser(userId);
        if (user.isEmpty()) {
            throw new UserIdNotFoundException(userId);
        }
        return user.get();
    }
}
