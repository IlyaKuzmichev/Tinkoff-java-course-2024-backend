package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.GithubLinks;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGithubLinkRepository extends JpaRepository<GithubLinks, Long> {
    @EntityGraph(attributePaths = {"links"})
    GithubLinks findGithubLinksById(Long id);
}
