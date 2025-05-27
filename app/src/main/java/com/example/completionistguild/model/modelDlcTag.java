package com.example.completionistguild.model;

public class modelDlcTag {
    public int users;
    public String link;

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public modelDlcTag(int users, String link) {
        this.users = users;
        this.link = link;
    }
}
