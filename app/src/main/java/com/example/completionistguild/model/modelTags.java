package com.example.completionistguild.model;

public class modelTags {
modelTag[] tags;
modelDlcTag[] dlcTags;

    public modelTag[] getTags() {
        return tags;
    }

    public void setTags(modelTag[] tags) {
        this.tags = tags;
    }

    public modelDlcTag[] getDlcTags() {
        return dlcTags;
    }

    public void setDlcTags(modelDlcTag[] dlcTags) {
        this.dlcTags = dlcTags;
    }

    public modelTags(modelTag[] tags, modelDlcTag[] dlcTags) {
        this.tags = tags;
        this.dlcTags = dlcTags;
    }
}
