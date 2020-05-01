package com.redditjokes.twistedjokes.userjokes.filtering_popup;

public class ListPopupItem {
    private String title;
    private int imageRes;

    public ListPopupItem(String title, int imageRes) {
        this.title = title;
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageRes() {
        return imageRes;
    }
}