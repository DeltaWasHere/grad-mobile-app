package com.example.completionistguild.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class modelGameView {

    @Expose
    public ArrayList<modelProduct> product = new ArrayList<>();
    @Expose
    public ArrayList<modelAchievement> achievements = new ArrayList<>();


    public ArrayList<modelProduct> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<modelProduct> product) {
        this.product = product;
    }

    public ArrayList<modelAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<modelAchievement> achievements) {
        this.achievements = achievements;
    }

    public modelGameView(ArrayList<modelProduct> product, ArrayList<modelAchievement> achievements) {
        this.product = product;
        this.achievements = achievements;
    }
}


