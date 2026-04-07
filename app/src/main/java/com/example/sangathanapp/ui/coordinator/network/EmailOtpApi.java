package com.example.sangathanapp.ui.coordinator.network;


import com.example.sangathanapp.ui.verification.network.ApiMessageResponse;
import com.example.sangathanapp.ui.verification.network.SendOtpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmailOtpApi {

    @POST("email/send-pin")
    Call<ApiMessageResponse> sendOtp(@Body SendOtpRequest request);
}