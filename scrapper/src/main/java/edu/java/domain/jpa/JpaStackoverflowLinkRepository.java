package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.StackoverflowLinks;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStackoverflowLinkRepository extends JpaRepository<StackoverflowLinks, Long> {
    Optional<StackoverflowLinks> findStackoverflowLinksById(Long id);
}
