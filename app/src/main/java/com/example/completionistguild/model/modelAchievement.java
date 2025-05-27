package com.example.completionistguild.model;

import com.google.gson.annotations.Expose;

public class modelAchievement {
    @Expose
    public String achievementId;

    public String gameId;

    @Expose
    public String name;
    @Expose
    public String description;
    @Expose
    public String icon;
    @Expose
    public Float rarity;
    @Expose
    public int achieved;
    @Expose
    public int dateAchieved;

    public String getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Float getRarity() {
        return rarity;
    }

    public void setRarity(Float rarity) {
        this.rarity = rarity;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public int getDateAchieved() {
        return dateAchieved;
    }

    public void setDateAchieved(int dateAchieved) {
        this.dateAchieved = dateAchieved;
    }

    public modelAchievement(String achievementId, String gameId, String name, String description, String icon, Float rarity, int achieved, int dateAchieved) {
        this.achievementId = achievementId;
this.gameId = gameId;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.rarity = rarity;
        this.achieved = achieved;
        this.dateAchieved = dateAchieved;
    }
}
