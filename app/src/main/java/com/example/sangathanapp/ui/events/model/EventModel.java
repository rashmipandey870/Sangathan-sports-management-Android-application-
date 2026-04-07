package com.example.sangathanapp.ui.events.model;

public class EventModel {

    private String teamA;
    private String teamB;
    private String matchDate;
    private String matchTime;
    private String venue;
    private String sport;

    public String getSport()
    {
        return sport;
    }
    public String getMatchTitle() {
        return teamA + " vs " + teamB;
    }

    public String getDate() {
        return matchDate;
    }

    public String getTime() {
        return matchTime;
    }

    public String getVenue() {
        return venue != null ? venue : "Venue not assigned";
    }
}