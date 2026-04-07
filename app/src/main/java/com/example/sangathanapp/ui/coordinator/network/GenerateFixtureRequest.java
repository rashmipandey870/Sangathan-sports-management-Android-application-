package com.example.sangathanapp.ui.coordinator.network;

import java.util.List;

public class GenerateFixtureRequest {

    private String sport;
    private String gender;
    private List<String> dates;
    private List<String> times;

    public GenerateFixtureRequest(String sport,
                                  String gender,
                                  List<String> dates,
                                  List<String> times) {
        this.sport = sport.toLowerCase();
        this.gender = gender.toUpperCase();
        this.dates = dates;
        this.times = times;
    }
}