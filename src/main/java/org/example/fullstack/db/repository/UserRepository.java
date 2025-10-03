package org.example.fullstack.db.repository;

import org.example.fullstack.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<Boolean> existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> getUserById(Long id);
}
