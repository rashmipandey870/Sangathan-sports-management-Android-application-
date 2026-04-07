package com.example.sangathanapp.ui.coordinator.network;

public class TrialRequest {

    private String teamName;

    private String teamId;
    private String date;
    private String time;
    private String venue;
    private String instructions;

    public TrialRequest(String teamId,String teamName, String date,
                        String time, String venue,
                        String instructions) {

        this.teamId = teamId;
        this.teamName=teamName;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.instructions = instructions;
    }
}