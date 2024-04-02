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
import edu.java.domain.mappers.GithubLinkInfoMapper;
import edu.java.domain.mappers.StackoverflowLinkInfoMapper;
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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaUserRepository userRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaGithubLinkRepository githubLinkRepository;
    private final JpaStackoverflowLinkRepository stackoverflowLinkRepository;
    private final JpaUserTrackedLinkRepository userTrackedLinkRepository;
    private final List<UpdateChecker> updateCheckers;

    @Override
    @Transactional
    public Link addLink(Long chatId, URI url) {
        Users user = checkUserAbility(chatId, User.Status.TRACK_LINK);
        user.setUserStatus(User.Status.BASE);
        userRepository.saveAndFlush(user);

        Link link = new Link(
            linkRepository.findByUrlIgnoreCase(url.toString())
                .map(Links::getId)
                .orElse(null),
            url
        );

        if (link.getId() == null) {
            addLinkToTables(link);
        }

        if (userTrackedLinkRepository.findUserTrackedLinksByUserIdAndLinkId(chatId, link.getId()).isPresent()) {
            throw new AttemptAddLinkOneMoreTimeException("You're already track this link");
        }
        UserTrackedLinks subscription = new UserTrackedLinks();
        subscription.setId(new UserTrackedLinksPK(chatId, link.getId()));
        userTrackedLinkRepository.save(subscription);
        return link;
    }

    @Transactional
    @Override
    public Link removeLinkByURL(Long chatId, URI url) {
        Users user = checkUserAbility(chatId, User.Status.UNTRACK_LINK);

        var optLink = linkRepository.findByUrlIgnoreCase(url.toString());
        if (optLink.isEmpty()) {
            throw new LinkNotFoundException("Link is not tracked by the service");
        }
        Links link = optLink.get();

        var optSubscription = userTrackedLinkRepository.findUserTrackedLinksByUserAndLink(user, link);

        if (optSubscription.isEmpty()) {
            throw new LinkNotFoundException("You don't track this link");
        }
        userTrackedLinkRepository.deleteByUserAndLink(user, link);
        if (userTrackedLinkRepository.findAllByLinkId(link.getId()).isEmpty()) {
            linkRepository.deleteById(link.getId());
        }
        return new Link(link.getId(), URI.create(link.getUrl()));
    }

    @Override
    public Collection<Link> findAllLinksForUser(Long chatId) {
        return userTrackedLinkRepository.findAllByUserId(chatId)
            .stream()
            .map(UserTrackedLinks::getLink)
            .map(link -> new Link(link.getId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Link> findLinksForUpdate(Long interval) {
        return linkRepository.findLinksByLastCheckBefore(OffsetDateTime.now().minusSeconds(interval))
            .stream()
            .map(link -> new Link(link.getId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public LinkInfo updateGithubLink(GithubLinkInfo linkInfo) {
        Long linkId = linkInfo.getLink().getId();
        resetLinkUpdateTime(linkId);

        var linkEntity = githubLinkRepository.findGithubLinksById(linkId);
        GithubLinkInfo oldLinkInfo = GithubLinkInfoMapper.entityToLinkInfo(linkEntity);
        GithubLinkInfoMapper.linkInfoToEntity(linkInfo, linkEntity);
        githubLinkRepository.save(linkEntity);
        return oldLinkInfo;
    }

    @Override
    @Transactional
    public LinkInfo updateStackoverflowLink(StackoverflowLinkInfo linkInfo) {
        Long linkId = linkInfo.getLink().getId();
        resetLinkUpdateTime(linkId);

        var linkEntity = stackoverflowLinkRepository.findStackoverflowLinksById(linkId);
        StackoverflowLinkInfo oldLinkInfo = StackoverflowLinkInfoMapper.entityToLinkInfo(linkEntity);
        StackoverflowLinkInfoMapper.linkInfoToEntity(linkInfo, linkEntity);
        stackoverflowLinkRepository.save(linkEntity);
        return oldLinkInfo;
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
                    jpaLink.setUrl(link.getUrl().toString());
                    jpaLink.setLastCheck(OffsetDateTime.now());
                    linkRepository.saveAndFlush(jpaLink);

                    link.setId(jpaLink.getId());
                    addLinkToSpecificTables(jpaLink, linkInfo);
                } catch (RuntimeException e) {
                    throw new IncorrectRequestParametersException("Incorrect link");
                }
            }
        }
    }

    private void addLinkToSpecificTables(Links link, LinkInfo linkInfo) {
        switch (link.getLinkType()) {
            case "github" -> {
                GithubLinks specLink = new GithubLinks();
                specLink.setLink(link);
                GithubLinkInfoMapper.linkInfoToEntity((GithubLinkInfo) linkInfo, specLink);
                githubLinkRepository.save(specLink);
            }
            case "stackoverflow" -> {
                StackoverflowLinks specLink = new StackoverflowLinks();
                specLink.setLink(link);
                StackoverflowLinkInfoMapper.linkInfoToEntity((StackoverflowLinkInfo) linkInfo, specLink);
                stackoverflowLinkRepository.save(specLink);
            }
            default -> throw new IncorrectRequestParametersException("Incorrect link type");
        }
    }

    private void resetLinkUpdateTime(Long linkId) {
        var optLink = linkRepository.findById(linkId);
        if (optLink.isPresent()) {
            Links link = optLink.get();
            link.setLastCheck(OffsetDateTime.now());
            linkRepository.saveAndFlush(link);
        }
    }
}
