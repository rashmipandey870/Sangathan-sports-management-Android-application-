package com.example.sangathanapp.ui.registration.network;

import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.SelectionRequest;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentRegistrationApi {

    @POST("register")
    Call<StudentRegistrationModel> registerStudent(
            @Body StudentRegistrationModel model
    );

    @POST("selection/update")
    Call<ApiResponse> updateSelection(@Body SelectionRequest request);

    @GET("team/students/{teamName}")
    Call<List<StudentRegistrationModel>> getTeamStudents();

    @PUT("select-player/{id}")
    Call<ApiResponse> selectPlayer(@Path("id") String id);


}