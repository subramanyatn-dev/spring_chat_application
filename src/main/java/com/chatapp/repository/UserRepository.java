package com.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if username exists
    boolean existsByUsername(String username);
}