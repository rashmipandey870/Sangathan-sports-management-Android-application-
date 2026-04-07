package com.example.sangathanapp.ui.verification.network;

public class SendOtpRequest {

    private String email;
    private int sportId;
    public SendOtpRequest(String email, int sportId) {
        this.email = email;
        this.sportId = sportId;
    }
}