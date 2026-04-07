package com.example.sangathanapp.ui.coordinator.network;

import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.SelectionRequest;
import com.example.sangathanapp.ui.coordinator.network.TeamViewResponse;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentApiService {

    // ✅ GET students of a team (for captain/coordinator)
    @GET("team/students/{teamName}")
    Call<List<StudentRegistrationModel>> getTeamStudents(
            @Path("teamName") String teamName
    );

    @PUT("select-player/{id}") Call<ApiResponse> selectPlayer(@Path("id") String id);

    @PUT("reject-player/{id}") Call<ApiResponse> rejectPlayer(@Path("id") String id);

    //  UPDATE selection
    @POST("selection/update-selection")
    Call<ApiResponse> updateSelection(
            @Body SelectionRequest request
    );

    // STUDENT VIEW TEAM (captain + players list)
    @GET("student/team/{teamName}")
    Call<TeamViewResponse> getStudentTeam(
            @Path("teamName") String teamName
    );

    @PUT("edit/{id}") Call<ApiResponse> editStudent(@Path("id") String id,@Body StudentRegistrationModel model);
}