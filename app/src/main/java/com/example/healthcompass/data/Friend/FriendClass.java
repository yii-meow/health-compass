package com.example.healthcompass.data.Friend;

import androidx.annotation.NonNull;

public class FriendClass {
    private String username;
    private String name;
    private String gender;
    private String status;

    public FriendClass() {
    }

    public FriendClass(String username, String name, String gender) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.status = "";
    }

    public FriendClass(String username, String name, String gender, String status) {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "FriendClass{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
