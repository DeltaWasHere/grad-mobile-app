package com.example.completionistguild.model;

public class modelThread {
    public String threadId;
    public String issue;
    public String title;
    public String content;
    public String response;
    public String media;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public modelThread(String issue, String title, String content, String response, String media, String threadId) {
        this.issue = issue;
        this.title = title;
        this.content = content;
        this.response = response;
        this.media = media;
        this.threadId = threadId;
    }
}
