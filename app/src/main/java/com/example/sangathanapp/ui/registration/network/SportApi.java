package com.example.sangathanapp.ui.registration.network;

import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.registration.SportModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SportApi {

    @GET("sports/years")
    Call<List<Integer>> getSportsYears();

    @GET("sports")
    Call<List<SportModel>> getSports(@Query("year") Integer year);

    @POST("sports")
    Call<ApiResponse> addSport(@Body SportModel sport);

    @DELETE("sports/{id}")
    Call<ApiResponse> removeSport(@Path("id") String id);

    @PUT("sports/{id}/coordinator")
    Call<ApiResponse> assignCoordinator(@Path("id") String id, @Body Map<String, String> body);

    @GET("sports/coordinator/{identifier}")
    Call<SportModel> getCoordinatorSport(@Path("identifier") String identifier);
}
