package com.example.completionistguild.model;

import java.util.ArrayList;

public class modelGameInfo {
    public String gameId;
    public String name;
    public int rate;
    public float completion_time;
    public String[] genres;
    public String front;

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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public float getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(float completion_time) {
        this.completion_time = completion_time;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public modelGameInfo(String gameId, String name, int rate, float completion_time, String[] genres, String front) {
        this.gameId = gameId;
        this.name = name;
        this.rate = rate;
        this.completion_time = completion_time;
        this.genres = genres;
        this.front = front;
    }
}
