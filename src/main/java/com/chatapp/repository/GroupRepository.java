package com.chatapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapp.entity.Group;
import com.chatapp.entity.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    
    // Find groups by owner
    List<Group> findByOwner(User owner);
    
    // Find groups by name containing (case-insensitive search)
    List<Group> findByGroupNameContainingIgnoreCase(String groupName);
    
    // Check if group exists by name
    boolean existsByGroupName(String groupName);
}