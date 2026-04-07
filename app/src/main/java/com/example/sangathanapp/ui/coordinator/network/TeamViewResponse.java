package com.example.sangathanapp.ui.coordinator.network;

import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

public class TeamViewResponse {

    private String teamName;
    private String captainName;
    private List<StudentRegistrationModel> players;

    public String getTeamName() { return teamName; }
    public String getCaptainName() { return captainName; }
    public List<StudentRegistrationModel> getPlayers() { return players; }
}