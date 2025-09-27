package com.chatapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.dto.CreateUserRequest;
import com.chatapp.dto.UpdateProfilePhotoRequest;
import com.chatapp.dto.UpdateUserRequest;
import com.chatapp.entity.User;
import com.chatapp.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // For development, configure appropriately for production
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            // Hash the password before creating user
            String hashedPassword = "hashed_" + request.getPassword(); // Simple hash for demo
            User user = userService.createUser(
                request.getUsername(),
                hashedPassword,
                request.getDisplayName()
            );
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<com.chatapp.dto.UserResponse>> getAllUsers() {
        List<UserService.UserResponse> serviceUsers = userService.getAllUsers();
        List<com.chatapp.dto.UserResponse> users = serviceUsers.stream()
            .map(sr -> new com.chatapp.dto.UserResponse(sr.getUserId(), sr.getUsername(), sr.getDisplayName()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
            .map(user -> new com.chatapp.dto.UserResponse(user.getUserId(), user.getUsername(), user.getDisplayName()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/profile-photo")
    public ResponseEntity<?> updateProfilePhoto(
            @PathVariable Integer userId,
            @RequestBody com.chatapp.dto.UpdateProfilePhotoRequest request) {
        try {
            User updatedUser = userService.updateProfilePhoto(userId, request.getProfilePhoto());
            return ResponseEntity.ok(new com.chatapp.dto.UserResponse(
                updatedUser.getUserId(), 
                updatedUser.getUsername(), 
                updatedUser.getDisplayName()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{username}/profile-photo")
    public ResponseEntity<?> deleteProfilePhoto(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            userService.updateProfilePhoto(user.getUserId(), User.DEFAULT_PROFILE_PHOTO);
            return ResponseEntity.ok("Profile photo removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateProfile(@PathVariable String username, @RequestBody UpdateUserRequest request) {
        try {
            com.chatapp.entity.User updatedUser = userService.updateUser(username, request);
            return ResponseEntity.ok(new com.chatapp.dto.UserResponse(
                updatedUser.getUserId(), 
                updatedUser.getUsername(), 
                updatedUser.getDisplayName()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{username}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable String username, @RequestBody UpdateProfilePhotoRequest request) {
        try {
            com.chatapp.entity.User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            userService.updateProfilePhoto(user.getUserId(), request.getProfilePhoto());
            return ResponseEntity.ok("Avatar updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

