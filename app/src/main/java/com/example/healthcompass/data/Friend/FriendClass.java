package com.example.healthcompass.data.Friend;

public class FriendClass {
    private String username;
    private String name;
    private String gender;

    public FriendClass() {
    }

    public FriendClass(String username, String name, String gender) {
        this.username = username;
        this.name = name;
        this.gender = gender;
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
}
