package com.example.completionistguild.model;

public class modelTag {
    public int users;
    public String tag;

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public modelTag(int users, String tag) {
        this.users = users;
        this.tag = tag;
    }
}
