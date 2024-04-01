package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.exception.IncorrectUserStatusException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.service.LinkService;
import edu.java.service.update_checker.UpdateChecker;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcUserRepository userRepository;
    private final List<UpdateChecker> updateCheckerList;

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
    public Link addLink(Long chatId, URI url) {
        User user = checkUserAbility(chatId, User.Status.TRACK_LINK);
        resetUserStatusToBase(user);

        Link link = new Link(null, url);

        for (UpdateChecker checker : updateCheckerList) {
            if (checker.isAppropriateLink(link)) {
                try {
                    LinkInfo linkInfo = checker.checkUpdates(link);
                    linkRepository.addLink(chatId, linkInfo, checker.getType());
                } catch (RuntimeException e) {
                    throw new IncorrectRequestParametersException("Incorrect link, please try again");
                } finally {
                    resetUserStatusToBase(user);
                }
            }
        }
        return link;
    }

    @Override
    public Link removeLinkByURL(Long chatId, URI url) {
        User user = checkUserAbility(chatId, User.Status.UNTRACK_LINK);
        resetUserStatusToBase(user);
        return linkRepository.removeLinkByURL(chatId, url);
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        return linkRepository.findAllLinksForUser(chatId);
    }

    @Override
    public Collection<Link> findLinksForUpdate(Long interval) {
        return linkRepository.findAllLinksWithCheckIntervalInSeconds(interval);
    }

    @Override
    public LinkInfo updateGithubLink(GithubLinkInfo linkInfo) {
        return linkRepository.updateGithubLink(linkInfo);
    }

    @Override
    public LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo) {
        return linkRepository.updateStackoverflowLink(linkInfo);
    }

    private User checkUserAbility(Long chatId, User.Status expectedStatus) {
        User user = findUser(chatId);
        User.Status prevStatus = user.getStatus();
        if (prevStatus != expectedStatus) {
            throw new IncorrectUserStatusException(chatId);
        }
        return user;
    }

    private void resetUserStatusToBase(User user) {
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
