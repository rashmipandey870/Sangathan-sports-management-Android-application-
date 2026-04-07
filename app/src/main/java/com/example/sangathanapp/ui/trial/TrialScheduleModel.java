package com.example.sangathanapp.ui.trial;
import com.google.gson.annotations.SerializedName;

public class TrialScheduleModel {

    private String date;
    private String time;
    private String venue;
    private String instructions;

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getVenue() { return venue; }
    public String getInstructions() { return instructions; }
}