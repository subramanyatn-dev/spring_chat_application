package com.chatapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.dto.AddMemberRequest;
import com.chatapp.dto.CreateGroupRequest;
import com.chatapp.entity.Group;
import com.chatapp.entity.GroupMember;
import com.chatapp.service.GroupService;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*") // For development, configure appropriately for production
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            // Validate group name
            if (request.getGroupName() == null || request.getGroupName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Group name cannot be empty");
            }

            Group group = groupService.createGroup(
                request.getGroupName().trim(),
                request.getOwnerId()
            );
            return ResponseEntity.ok(group);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMember(
            @PathVariable Integer groupId,
            @RequestBody AddMemberRequest request) {
        try {
            GroupMember member = groupService.addMember(groupId, request.getUserId());
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Integer groupId) {
        try {
            List<GroupMember> members = groupService.getGroupMembers(groupId);
            return ResponseEntity.ok(members);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

        @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<?> removeMember(
            @PathVariable Integer groupId,
            @PathVariable Integer userId) {
        try {
            groupService.removeMember(groupId, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
}
}
