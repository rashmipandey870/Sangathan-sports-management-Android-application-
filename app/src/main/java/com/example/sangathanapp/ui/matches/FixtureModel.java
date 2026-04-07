package com.example.sangathanapp.ui.matches;

import com.google.gson.annotations.SerializedName;

public class FixtureModel {

    @SerializedName("_id")
    private String id;

    private String teamA;
    private String teamB;

    private String matchDate;
    private String matchTime;
    private String venue;

    public String getId() {
        return id;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public String getVenue() {
        return venue;
    }
}