package com.chatapp.dto;

public class UserResponse {
    private Integer id;
    private String username;
    private String displayName;

    // Constructor
    public UserResponse(Integer id, String username, String displayName) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}