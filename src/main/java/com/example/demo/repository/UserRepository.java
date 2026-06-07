package com.example.demo.repository;

import com.example.demo.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByUsernameAndIsActiveTrue(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
