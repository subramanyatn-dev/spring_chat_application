package com.chatapp.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    // Default profile photo URL - Professional avatar
    public static final String DEFAULT_PROFILE_PHOTO = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // Map to user_id column
    private Integer userId; // Changed to Integer to match database INT type

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @Column(name = "profile_photo", length = 500)
    private String profilePhoto;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor
    public User() {
        this.createdAt = LocalDateTime.now();
        this.profilePhoto = DEFAULT_PROFILE_PHOTO; // Set default profile photo
    }

    // Parameterized constructor
    public User(String username, String passwordHash, String displayName) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private Set<Group> ownedGroups;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private Set<Message> receivedMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<GroupMember> groupMemberships;

    public Set<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(Set<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<GroupMember> getGroupMemberships() {
        return groupMemberships;
    }

    public void setGroupMemberships(Set<GroupMember> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}