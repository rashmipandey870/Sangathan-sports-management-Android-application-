package com.example.sangathanapp.ui.coordinator.network;

public class SelectionRequest {

    private String studentId;
    private String status;

    public SelectionRequest(String studentId, String status) {
        this.studentId = studentId;
        this.status = status;
    }
}