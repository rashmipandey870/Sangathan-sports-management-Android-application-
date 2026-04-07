package com.example.sangathanapp.ui.profile.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfileApi {

    // CREATE profile (after registration)
    @POST("profile")
    Call<ProfileModel> createProfile(@Body ProfileModel profile);

    // GET profile
    @GET("profile/{email}")
    Call<ProfileModel> getProfile(@Path("email") String email);

    // UPDATE profile
    @PUT("profile/{email}")
    Call<ProfileModel> updateProfile(
            @Path("email") String email,
            @Body ProfileModel profile
    );

    // ADD achievement
    @POST("profile/{email}/achievement")
    Call<ProfileModel> addAchievement(
            @Path("email") String email,
            @Body AchievementModel achievement
    );

    // UPDATE stats
    @PUT("profile/{email}/stats")
    Call<ProfileModel> updateStats(
            @Path("email") String email,
            @Body StatsModel stats
    );
}