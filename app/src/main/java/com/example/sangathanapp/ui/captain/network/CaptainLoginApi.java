package com.example.sangathanapp.ui.captain.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CaptainLoginApi {

    @POST("captain/login")
    Call<CaptainLoginResponse> captainlogin(
            @Body CaptainLoginRequest request
    );
}