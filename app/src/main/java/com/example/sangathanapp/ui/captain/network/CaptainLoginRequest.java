package com.example.sangathanapp.ui.captain.network;

public class CaptainLoginRequest {
    private String teamName;

    private String pin;

    public CaptainLoginRequest(String teamName,String pin) {
        this.pin = pin;
        this.teamName=teamName;
    }

    public String getTeamName() {
        return teamName;
    }
    public String getPin(){
        return pin;
    }
}