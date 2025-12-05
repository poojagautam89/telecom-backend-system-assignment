package com.telecom.api.repository;

import com.telecom.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);   // already present

    boolean existsByUsername(String username);        // <-- ADD THIS
}
