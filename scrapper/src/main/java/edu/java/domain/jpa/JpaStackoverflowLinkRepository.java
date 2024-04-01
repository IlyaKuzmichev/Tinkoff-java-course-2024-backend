package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.StackoverflowLinks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStackoverflowLinkRepository extends JpaRepository<StackoverflowLinks, Long> {
}
