package com.example.myfashionstore.data;

public class PostModel {

    private String currentDate;
    private String currentTime;
    private String postDescription;
    private String shirtImageUrl;
    private String shortImageUrl;

    public PostModel() {
    }

    public PostModel(String currentDate, String currentTime, String postDescription, String shirtImageUrl, String shortImageUrl) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.postDescription = postDescription;
        this.shirtImageUrl = shirtImageUrl;
        this.shortImageUrl = shortImageUrl;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getShirtImageUrl() {
        return shirtImageUrl;
    }

    public void setShirtImageUrl(String shirtImageUrl) {
        this.shirtImageUrl = shirtImageUrl;
    }

    public String getShortImageUrl() {
        return shortImageUrl;
    }

    public void setShortImageUrl(String shortImageUrl) {
        this.shortImageUrl = shortImageUrl;
    }

}
