package com.example.sangathanapp.ui.trial.network;

import com.example.sangathanapp.ui.trial.TrialScheduleModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StudentTrialApi {

    @GET("trials/teamByName/{teamName}")
    Call<List<com.example.sangathanapp.ui.trial.TrialScheduleModel>>
    getTrialByTeamName(@Path("teamName") String teamName);


}