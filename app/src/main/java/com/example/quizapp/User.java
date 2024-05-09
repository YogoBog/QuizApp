package com.example.quizapp;

import com.google.firebase.Timestamp;

public class User {
    private String username;
    private String profilePictureUrl;
    private int userPoints;

    // Default constructor
    public User() {
    }

    // Constructor with arguments
    public User(String username, String profilePictureUrl, int userPoints) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.userPoints = userPoints;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }
}

