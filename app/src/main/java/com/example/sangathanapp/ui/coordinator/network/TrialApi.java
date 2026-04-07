package com.example.sangathanapp.ui.coordinator.network;



import com.example.sangathanapp.ui.trial.TrialScheduleModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrialApi {

    // ADMIN: create trial
    @POST("trials/create")
    Call<ApiResponse> createTrial(@Body TrialRequest request);

    @GET("trials/team/{teamId}") Call<List<TrialScheduleModel>>
    getTrialByTeam(@Path("teamId") String teamId);


}