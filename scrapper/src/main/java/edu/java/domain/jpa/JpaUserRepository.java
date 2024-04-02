package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<Users, Long> {
    @EntityGraph(attributePaths = {"user_tracked_links"})
    Users findUsersById(Long id);
}
