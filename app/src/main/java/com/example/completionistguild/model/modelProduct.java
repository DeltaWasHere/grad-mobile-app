package com.example.completionistguild.model;

import com.google.gson.annotations.Expose;

public class modelProduct {
    @Expose
    public String link;
    @Expose
    public String price;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public modelProduct(String link, String price) {
        this.link = link;
        this.price = price;
    }
}
