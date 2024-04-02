package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Links;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<Links, Long> {
    Optional<Links> findByUrlIgnoreCase(String url);

    List<Links> findLinksByLastCheckBefore(OffsetDateTime lastCheck);
}
