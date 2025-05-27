package com.example.completionistguild.model;

public class modelLeaderEntry {
    public String name;
    public String avatar;
    public int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public modelLeaderEntry(String name, String avatar, int score) {
        this.name = name;
        this.avatar = avatar;
        this.score = score;
    }
}
