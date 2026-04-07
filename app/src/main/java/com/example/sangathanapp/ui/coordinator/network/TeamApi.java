package com.example.sangathanapp.ui.coordinator.network;

import com.example.sangathanapp.ui.registration.StudentRegistrationModel;
import com.example.sangathanapp.ui.registration.network.TeamModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TeamApi {

    @POST("create-team")
    Call<CreateTeamResponse> createTeam(@Body CreateTeamRequest request);

    @GET("teams")
    Call<List<TeamModel>> getTeams();

    @GET("team/{teamName}")
    Call<TeamViewResponse> getTeamPlayers(@Path("teamName") String teamName, @Query("type") String type);

    @GET("departments") Call<List<String>>
    getDepartments();

    @GET("team/students/{teamName}")
    Call<List<StudentRegistrationModel>> getTeamStudents(@Path("teamName") String teamName);

    @PUT("select-player/{id}")
    Call<ApiResponse> selectPlayer(@Path("id") String id);

}

