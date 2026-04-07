package com.example.sangathanapp.ui.coordinator.network;

import java.util.List;

public class AddTeamsRequest {

    private String sport;
    private List<String> teams;

    public AddTeamsRequest(String sport, List<String> teams) {
        this.sport = sport.toLowerCase();
        this.teams = teams;
    }
}