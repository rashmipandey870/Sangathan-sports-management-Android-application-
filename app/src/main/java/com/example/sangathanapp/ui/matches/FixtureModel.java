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
    private Integer round;
    private String winner;
    private String scoreA;
    private String scoreB;

    public String getScoreA() {
        return scoreA;
    }

    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    public String getScoreB() {
        return scoreB;
    }

    public void setScoreB(String scoreB) {
        this.scoreB = scoreB;
    }

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

    public Integer getRound() {
        return round;
    }

    public String getWinner() {
        return winner;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}