package com.chatapp.dto;

public class UpdateUserRequest {
    private String displayName;
    private String password; // Optional - only if user wants to change password
    private String profilePhoto; // Optional - only if user wants to change profile photo

    // Getters and Setters
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}