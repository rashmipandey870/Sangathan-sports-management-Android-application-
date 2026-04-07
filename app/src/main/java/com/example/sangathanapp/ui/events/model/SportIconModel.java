package com.example.sangathanapp.ui.events.model;

public class SportIconModel {

    private final String sport;   // cricket, football
    private final int iconRes;

    public SportIconModel(String sport, int iconRes) {
        this.sport = sport;
        this.iconRes = iconRes;
    }

    public String getSport() {
        return sport;
    }

    public int getIconRes() {
        return iconRes;
    }
}