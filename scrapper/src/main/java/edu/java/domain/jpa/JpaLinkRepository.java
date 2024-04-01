package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Links;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkRepository extends JpaRepository<Links, Long> {
    Optional<Links> findByUrl(URI url);
}
