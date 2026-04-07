package com.example.sangathanapp.ui.registration.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TeamApi {

    @GET("departments") Call<List<String>>
    getDepartments();


    //  FILTER TEAMS (for registration dropdown)
    @GET("filter")
    Call<List<TeamModel>> getFilteredTeams(
            @Query("department") String department,
            @Query("gender") String gender,
            @Query("sport") String sport
    );




    //  GET ALL TEAMS (for coordinator)
    @GET("teams")
    Call<List<TeamModel>> getTeams();
}