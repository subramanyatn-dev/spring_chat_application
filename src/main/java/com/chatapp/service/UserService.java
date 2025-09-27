package com.chatapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.dto.UpdateUserRequest;
import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String passwordHash, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setDisplayName(displayName);
        
        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getUserId(), user.getUsername(), user.getDisplayName()))
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public User updateProfilePhoto(Integer userId, String profilePhoto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePhoto(profilePhoto);
        return userRepository.save(user);
    }

    public void removeProfilePhoto(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePhoto(User.DEFAULT_PROFILE_PHOTO);
        userRepository.save(user);
    }

    public User updateUser(String username, UpdateUserRequest request) {
        User user = findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (request.getDisplayName() != null && !request.getDisplayName().trim().isEmpty()) {
            user.setDisplayName(request.getDisplayName());
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPasswordHash("hashed_" + request.getPassword());
        }
        
        if (request.getProfilePhoto() != null) {
            user.setProfilePhoto(request.getProfilePhoto());
        }
        
        return userRepository.save(user);
    }

    // Simple DTO defined as a nested class to avoid missing external dependency
    public static class UserResponse {
        private Integer userId;
        private String username;
        private String displayName;

        public UserResponse(Integer userId, String username, String displayName) {
            this.userId = userId;
            this.username = username;
            this.displayName = displayName;
        }

        public Integer getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}