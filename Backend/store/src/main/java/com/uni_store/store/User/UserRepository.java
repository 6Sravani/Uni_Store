package com.uni_store.store.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByIsActiveTrue();

    List<User> findByRole(UserRole role);

    List<User> findByEmailVerified(Boolean emailVerified);

    Page<User> findByIsActiveTrue(Pageable pageable);
}
