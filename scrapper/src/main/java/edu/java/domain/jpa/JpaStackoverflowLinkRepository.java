package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.StackoverflowLinks;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStackoverflowLinkRepository extends JpaRepository<StackoverflowLinks, Long> {
    @EntityGraph(attributePaths = {"links"})
    StackoverflowLinks findStackoverflowLinksById(Long id);
}
