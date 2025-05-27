package com.example.completionistguild.model;

public class modelProfile {
    public  String userId;
    public int position;
    public int score;
    public String avatar;
    public String name;
    public modelAchievement[] valuableAchievements;
    public modelAchievement[] recentAchievements;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;}
    public modelAchievement[] getValuableAchievements() {
        return valuableAchievements;
    }

    public void setValuableAchievements(modelAchievement[] valuableAchievements) {
        this.valuableAchievements = valuableAchievements;
    }

    public modelAchievement[] getRecentAchievements() {
        return recentAchievements;
    }

    public void setRecentAchievements(modelAchievement[] recentAchievements) {
        this.recentAchievements = recentAchievements;
    }


    public modelProfile(String userId, String avatar, String name, int position, int score, modelAchievement[] valuableAchievements, modelAchievement[] recentAchievements) {
        this.userId = userId;
        this.avatar = avatar;
        this.name = name;
        this.position = position;
        this.score = score;
        this.valuableAchievements = valuableAchievements;
        this.recentAchievements = recentAchievements;
    }
}
