package com.example.sangathanapp.ui.headadmin.network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HeadAdminApi {

    @GET("admin/stats")
    Call<Map<String, Integer>> getGeneralStats();

    @GET("admin/leaderboard")
    Call<List<LeaderboardEntry>> getLeaderboard(
            @retrofit2.http.Query("gender") String gender,
            @retrofit2.http.Query("sport") String sport
    );
}
