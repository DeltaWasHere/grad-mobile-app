package com.example.completionistguild.model;

public class modelGuides {
    public String name;
    public String guideId;
    public int date;
    public int votes;
    public String avatar;
    public String content;
    public String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public modelGuides(String name, String guideId, int date, int votes, String avatar, String content, String userId) {
        this.name = name;
        this.guideId = guideId;
        this.date = date;
        this.votes = votes;
        this.avatar = avatar;
        this.content = content;
        this.userId= userId;
    }
}
