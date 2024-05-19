package com.example.healthcompass.data.User;

import java.util.Map;

public class UserClass {
    String name, username, password, gender;
    int age;
    float height, weight;
    Map<String, Map<String, Boolean>> milestones;

    public UserClass() {
    }

    public UserClass(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserClass(String name, String username, String password, String gender, int age, float height, float weight, Map<String, Map<String, Boolean>> milestones) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.milestones = milestones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Map<String, Map<String, Boolean>> getMilestones() {
        return milestones;
    }

    public void setMilestones(Map<String, Map<String, Boolean>> milestones) {
        this.milestones = milestones;
    }
}
