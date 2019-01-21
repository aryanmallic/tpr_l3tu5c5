package com.app.letuscs.models.homeModel;

public class ModelHomeGrids {
    private String title;
    private int imageUrl;

    public ModelHomeGrids(String title, int imageUrl){

        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}
