package com.example.sangathanapp.ui.verification.network;

public class VerifyOtpRequest {

    private String email;
    private int sportId;
    private String pin;
    public VerifyOtpRequest(String email, int sportId, String pin) {
        this.email = email;
        this.sportId = sportId;
        this.pin = pin;
    }
}