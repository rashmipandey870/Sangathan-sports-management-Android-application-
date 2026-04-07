package com.example.sangathanapp.ui.registration.network;

import com.google.gson.annotations.SerializedName;

public class TeamModel {

    @SerializedName("_id")
    private String id;

    private String teamName;
    private String captainName;
    private String enrollmentNo;
    private String department;
    private String gender;
    private String sport;
    private String pin;

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCaptainName() {
        return captainName;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public String getSport() {
        return sport;
    }

    public String getPin() {
        return pin;
    }
}