package edu.java.service.jpa;

import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackoverflowLinkRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.domain.jpa.JpaUserTrackedLinkRepository;
import edu.java.domain.jpa.entities.GithubLinks;
import edu.java.domain.jpa.entities.Links;
import edu.java.domain.jpa.entities.StackoverflowLinks;
import edu.java.domain.jpa.entities.UserTrackedLinks;
import edu.java.domain.jpa.entities.UserTrackedLinksPK;
import edu.java.domain.jpa.entities.Users;
import edu.java.exception.AttemptAddLinkOneMoreTimeException;
import edu.java.exception.IncorrectRequestParametersException;
import edu.java.exception.IncorrectUserStatusException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UserIdNotFoundException;
import edu.java.models.GithubLinkInfo;
import edu.java.models.Link;
import edu.java.models.LinkInfo;
import edu.java.models.StackoverflowLinkInfo;
import edu.java.models.User;
import edu.java.service.LinkService;
import edu.java.service.update_checker.UpdateChecker;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaUserRepository userRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaGithubLinkRepository githubLinkRepository;
    private final JpaStackoverflowLinkRepository stackoverflowLinkRepository;
    private final JpaUserTrackedLinkRepository userTrackedLinkRepository;
    private final List<UpdateChecker> updateCheckers;

    @Override
    public Link addLink(Long chatId, URI url) {
        Users user = checkUserAbility(chatId, User.Status.TRACK_LINK);
        user.setUserStatus(User.Status.BASE);
        userRepository.saveAndFlush(user);

        Link link = new Link(
            linkRepository.findByUrl(url)
            .map(Links::getId)
            .orElse(null),
            url);

        if (link.getId() == null) {
            addLinkToTables(link);
        }

        if (userTrackedLinkRepository.findUserTrackedLinksByUserIdAndLinkId(chatId, link.getId()).isPresent()) {
            throw new AttemptAddLinkOneMoreTimeException("You're already track this link");
        }
        UserTrackedLinks subscription = new UserTrackedLinks();
        subscription.setId(new UserTrackedLinksPK(chatId, link.getId()));
        userTrackedLinkRepository.saveAndFlush(subscription);
        return link;
    }

    @Override
    public Link removeLinkByURL(Long chatId, URI url) {
        var optUser = userRepository.findById(chatId);
        if (optUser.isEmpty()) {
            throw new UserIdNotFoundException(chatId);
        }
        Users user = optUser.get();

        var optLink = linkRepository.findByUrl(url);
        if (optLink.isEmpty()) {
            throw  new LinkNotFoundException("Link is not tracked by the service");
        }
        Links link = optLink.get();

        var optSubscription = userTrackedLinkRepository.findUserTrackedLinksByUserAndLink(user, link);

        if (optSubscription.isEmpty()) {
            throw new LinkNotFoundException("You don't track this link");
        }
        userTrackedLinkRepository.deleteByUserAndLink(user, link);
        userTrackedLinkRepository.flush();
        if (userTrackedLinkRepository.findAllByLinkId(link.getId()).isEmpty()) {
            linkRepository.deleteById(link.getId());
        }
        return new Link(link.getId(), link.getUrl());
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        return userTrackedLinkRepository.findAllByUserId(chatId)
            .stream()
            .map(UserTrackedLinks::getLink)
            .map(link -> new Link(link.getId(), link.getUrl()))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Link> findLinksForUpdate(Long interval) {
        return null;
    }

    @Override
    public LinkInfo updateGithubLink(GithubLinkInfo linkInfo) {
        return null;
    }

    @Override
    public LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo) {
        return null;
    }

    private Users checkUserAbility(Long userId, User.Status status) {
        var optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new UserIdNotFoundException(userId);
        }
        Users user = optUser.get();
        if (user.getUserStatus() != status) {
            throw new IncorrectUserStatusException(userId);
        }
        return user;
    }

    private void addLinkToTables(Link link) {
        for (UpdateChecker checker : updateCheckers) {
            if (checker.isAppropriateLink(link)) {
                try {
                    LinkInfo linkInfo = checker.checkUpdates(link);
                    Links jpaLink = new Links();
                    jpaLink.setLinkType(checker.getType());
                    jpaLink.setUrl(link.getUrl());
                    jpaLink.setLastCheck(OffsetDateTime.now());
                    link.setId(jpaLink.getId());
                    linkRepository.save(jpaLink);
                    addLinkToSpecificTables(link.getId(), linkInfo, checker.getType());
                } catch (RuntimeException e) {
                    throw new IncorrectRequestParametersException("Incorrect link");
                }
            }
        }
    }

    private void addLinkToSpecificTables(Long linkId, LinkInfo linkInfo, String linkType) {

        switch (linkType) {
            case "github" -> {
                GithubLinks link = new GithubLinks();
                link.setId(linkId);
                link.setLastUpdate(((GithubLinkInfo) linkInfo).getUpdateTime());
                link.setLastPush(((GithubLinkInfo) linkInfo).getPushTime());
                link.setPullRequestsCount(((GithubLinkInfo) linkInfo).getPullRequestsCount());
                githubLinkRepository.save(link);
            }
            case "stackoverflow" -> {
                StackoverflowLinks link = new StackoverflowLinks();
                link.setId(linkId);
                link.setLastUpdate(((StackoverflowLinkInfo) linkInfo).getUpdateTime());
                link.setAnswersCount(((StackoverflowLinkInfo) linkInfo).getAnswersCount());
                stackoverflowLinkRepository.save(link);
            }
            default -> throw new IncorrectRequestParametersException("Incorrect link type");
        }
    }

}
