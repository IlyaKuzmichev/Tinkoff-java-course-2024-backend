package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.GithubLinks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGithubLinkRepository extends JpaRepository<GithubLinks, Long> {
}
