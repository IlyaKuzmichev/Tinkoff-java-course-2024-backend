package edu.java.service.jooq;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.exception.IncorrectRequestParametersException;
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

public class JooqLinkService implements LinkService {
    private final JooqLinkRepository linkRepository;
    private final JooqUserRepository userRepository;
    private final List<UpdateChecker> updateCheckerList;

    public JooqLinkService(JooqLinkRepository linkRepository,
        JooqUserRepository userRepository, List<UpdateChecker> updateCheckerList) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.updateCheckerList = updateCheckerList;
    }

    @Override
    public Link addLink(Long chatId, URI url) {
        checkUserAndResetStatus(chatId);

        LinkInfo linkInfo;
        Link link = new Link(null, url);
        for (UpdateChecker checker : updateCheckerList) {
            if (checker.isAppropriateLink(link)) {
                try {
                    linkInfo = checker.checkUpdates(link);
                } catch (RuntimeException e) {
                    throw new IncorrectRequestParametersException("Incorrect link, please try again");
                }
                linkRepository.addLink(chatId, linkInfo, checker.getType());
            }
        }
        return link;
    }

    @Override
    public Link removeLinkByURL(Long chatId, URI url) {
        checkUserAndResetStatus(chatId);
        return linkRepository.removeLinkByURL(chatId, url);
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        checkUserAndResetStatus(chatId);
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

    private void checkUserAndResetStatus(Long userId) {
        Optional<User> optUser = userRepository.findUser(userId);
        if (optUser.isEmpty()) {
            throw new UserIdNotFoundException(userId);
        }
        User user = optUser.get();
        user.setStatus(User.Status.BASE);
        userRepository.updateUser(user);
    }
}
