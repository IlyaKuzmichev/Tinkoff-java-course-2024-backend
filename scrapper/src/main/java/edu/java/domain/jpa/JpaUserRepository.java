package edu.java.domain.jpa;

import edu.java.domain.jpa.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<Users, Long> {
}
