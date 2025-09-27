package com.chatapp.dto;

public class UpdateProfilePhotoRequest {
    private String profilePhoto;

    // Default constructor
    public UpdateProfilePhotoRequest() {}

    // Constructor
    public UpdateProfilePhotoRequest(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    // Getters and setters
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}