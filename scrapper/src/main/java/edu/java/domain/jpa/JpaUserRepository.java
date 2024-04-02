package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<Users, Long> {
    @EntityGraph(attributePaths = {"trackedLinks"})
    Optional<Users> findUsersById(Long id);
}
