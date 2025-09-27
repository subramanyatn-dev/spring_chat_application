package com.chatapp.dto;

public class UserDto {
    private Integer userId;
    private String username;
    private String displayName;
    private String profilePhoto;
    
    // Default constructor
    public UserDto() {}
    
    // Constructor to convert from User entity
    public UserDto(Integer userId, String username, String displayName) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
    }

    // Constructor with profile photo
    public UserDto(Integer userId, String username, String displayName, String profilePhoto) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.profilePhoto = profilePhoto;
    }
    
    // Getters and setters
    public Integer getId() {
        return userId;
    }
    
    public void setId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}