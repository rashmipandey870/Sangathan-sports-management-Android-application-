package com.example.sangathanapp.ui.coordinator.network;

public class CreateTeamRequest {

    private String teamName;
    private String captainName;
    private String enrollment;
    private String department;
    private String gender;
    private String sport;

    public CreateTeamRequest(
            String teamName,
            String captainName,
            String enrollment,
            String department,
            String gender,
            String sport
    ) {
        this.teamName = teamName;
        this.captainName = captainName;
        this.enrollment = enrollment;
        this.department = department;
        this.gender = gender;
        this.sport = sport;
    }
}