package com.example.sangathanapp.ui.coordinator.network;

import com.example.sangathanapp.ui.matches.FixtureModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FixtureApi {


    @POST("admin/add-teams")
    Call<ApiResponse> addTeams(
            @Body AddTeamsRequest request
    );


    @POST("admin/generate-fixture")
    Call<ApiResponse> generateFixture(
            @Body GenerateFixtureRequest request
    );



    @GET("fixtures")
    Call<List<FixtureModel>> getAllFixtures(
            @Query("limit") Integer limit
    );

    @GET("fixtures/{sport}")
    Call<List<FixtureModel>> getFixturesBySport(
            @Path("sport") String sport,
            @Query("gender") String gender,
            @Query("round") Integer round
    );

    @POST("admin/update-winner/{id}")
    Call<ApiResponse> updateWinner(
            @Path("id") String fixtureId,
            @Body Map<String, String> body
    );

    @POST("admin/update-venue/{id}")
    Call<ApiResponse> updateVenue(
            @Path("id") String fixtureId,
            @Body Map<String, String> body
    );


}