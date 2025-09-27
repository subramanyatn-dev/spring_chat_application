package com.chatapp.repository;

import com.chatapp.entity.Group;
import com.chatapp.entity.GroupMember;
import com.chatapp.entity.GroupMemberId;
import com.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    
    // Find all groups a user is member of
    List<GroupMember> findByUser(User user);
    
    // Find all members of a group
    List<GroupMember> findByGroup(Group group);
    
    // Check if user is member of group
    boolean existsByGroupAndUser(Group group, User user);
    
    // Delete member from group
    void deleteByGroupAndUser(Group group, User user);
}