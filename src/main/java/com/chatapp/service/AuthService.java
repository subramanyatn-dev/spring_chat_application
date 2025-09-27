package com.chatapp.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.dto.LoginResponse;
import com.chatapp.dto.UserDto;
import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;
import com.chatapp.utils.DtoConverter;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Simple password verification (in production, use proper password hashing)
        String expectedHash = "hashed_" + password;
        if (!user.getPasswordHash().equals(expectedHash)) {
            throw new RuntimeException("Invalid password");
        }

        // Generate session ID (in production, use proper session management)
        String sessionId = UUID.randomUUID().toString();
        
        UserDto userDto = DtoConverter.convertToDto(user);
        return new LoginResponse("Login successful", userDto, sessionId);
    }
}