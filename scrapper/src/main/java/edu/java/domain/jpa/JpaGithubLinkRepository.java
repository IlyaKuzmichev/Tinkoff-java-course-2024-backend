package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.GithubLinks;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGithubLinkRepository extends JpaRepository<GithubLinks, Long> {
    Optional<GithubLinks> findGithubLinksById(Long id);
}
