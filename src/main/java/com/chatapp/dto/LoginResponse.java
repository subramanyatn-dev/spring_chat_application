package com.chatapp.dto;

public class LoginResponse {
    private String message;
    private UserDto user;
    private String sessionId;

    public LoginResponse(String message, UserDto user, String sessionId) {
        this.message = message;
        this.user = user;
        this.sessionId = sessionId;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}