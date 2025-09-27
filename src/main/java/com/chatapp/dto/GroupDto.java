package com.chatapp.dto;

public class GroupDto {
    private Integer groupId;
    private String groupName;
    private String profilePhoto;
    private String groupUsername;
    private UserDto owner;

    // Default constructor
    public GroupDto() {}

    // Constructor
    public GroupDto(Integer groupId, String groupName, String profilePhoto, String groupUsername, UserDto owner) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.profilePhoto = profilePhoto;
        this.groupUsername = groupUsername;
        this.owner = owner;
    }

    // Getters and setters
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getGroupUsername() {
        return groupUsername;
    }

    public void setGroupUsername(String groupUsername) {
        this.groupUsername = groupUsername;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }
}