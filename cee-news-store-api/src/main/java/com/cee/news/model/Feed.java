package com.cee.news.model;


public class Feed {

    private String location;

    private String contentType;

    private String title;

    private boolean active;

    public Feed(String location, String title, String contentType) {
        this.location = location;
        this.contentType = contentType;
        this.title = title;
        active = false;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}