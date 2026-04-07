package com.example.sangathanapp.ui.verification.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmailVerificationApi {
    @POST("email/send-pin")
    Call<ApiMessageResponse> sendOtp(@Body SendOtpRequest request);

    @POST("email/verify-pin")
    Call<ApiMessageResponse> verifyOtp(@Body VerifyOtpRequest request);
}