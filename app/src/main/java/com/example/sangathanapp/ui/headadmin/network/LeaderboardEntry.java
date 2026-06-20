package com.example.sangathanapp.ui.headadmin.network;

import com.google.gson.annotations.SerializedName;

public class LeaderboardEntry {

    @SerializedName("department")
    private String department;

    @SerializedName("wins")
    private int wins;

    public LeaderboardEntry(String department, int wins) {
        this.department = department;
        this.wins = wins;
    }

    public String getDepartment() {
        return department;
    }

    public int getWins() {
        return wins;
    }
}
