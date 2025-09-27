package com.chatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.entity.Group;
import com.chatapp.entity.GroupMember;
import com.chatapp.entity.User;
import com.chatapp.repository.GroupMemberRepository;
import com.chatapp.repository.GroupRepository;
import com.chatapp.repository.UserRepository;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, 
                       UserRepository userRepository,
                       GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * Create a new group
     */
    public Group createGroup(String groupName, Integer ownerId) {
        User owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (groupRepository.existsByGroupName(groupName)) {
            throw new RuntimeException("Group name already exists");
        }

        Group group = new Group();
        group.setGroupName(groupName);
        group.setOwner(owner);
        
        group = groupRepository.save(group);

        // Add owner as first member
        addMember(group.getGroupId(), ownerId);

        return group;
    }

    /**
     * Add a member to a group
     */
    public GroupMember addMember(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is already a member of this group");
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);

        return groupMemberRepository.save(member);
    }

    /**
     * Get all members of a group
     */
    public List<GroupMember> getGroupMembers(Integer groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMemberRepository.findByGroup(group);
    }

    /**
     * Get all groups a user is member of
     */
    public List<GroupMember> getUserGroups(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return groupMemberRepository.findByUser(user);
    }

    /**
     * Remove a member from a group
     */
    public void removeMember(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is not a member of this group");
        }

        // Cannot remove the owner
        if (group.getOwner().getUserId().equals(userId)) {
            throw new RuntimeException("Cannot remove the group owner");
        }

        groupMemberRepository.deleteByGroupAndUser(group, user);
    }
}