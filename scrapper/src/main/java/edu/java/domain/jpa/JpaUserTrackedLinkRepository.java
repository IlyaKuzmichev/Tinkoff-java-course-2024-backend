package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Links;
import edu.java.domain.jpa.entities.UserTrackedLinks;
import edu.java.domain.jpa.entities.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserTrackedLinkRepository extends JpaRepository<UserTrackedLinks, Long> {
    List<UserTrackedLinks> findAllByUserId(Long id);

    List<UserTrackedLinks> findAllByLinkId(Long id);

    Optional<UserTrackedLinks> findUserTrackedLinksByUserIdAndLinkId(Long userId, Long linkId);

    Optional<UserTrackedLinks> findUserTrackedLinksByUserAndLink(Users user, Links link);

    void deleteByUserAndLink(Users user, Links link);
}
