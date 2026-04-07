package com.example.sangathanapp.ui.verification;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.verification.network.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText etPin;
    private Button btnVerify;
    private TextView tvResend;
    private ImageView btnBack;

    private String userEmail;
    private int sportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        userEmail = getIntent().getStringExtra("USER_EMAIL");
        sportId = getIntent().getIntExtra("SPORT_ID", -1);

        etPin = findViewById(R.id.et_verification_pin);
        btnVerify = findViewById(R.id.btn_verify_pin);
        tvResend = findViewById(R.id.tv_resend_pin);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());


        btnVerify.setOnClickListener(v -> verifyOtp());

    }



    private void verifyOtp() {

        String pin = etPin.getText().toString().trim();

        if (pin.length() != 6) {
            etPin.setError("Enter valid 6-digit OTP");
            return;
        }

        EmailVerificationApi api =
                ApiClient.getClient().create(EmailVerificationApi.class);

        api.verifyOtp(new VerifyOtpRequest(userEmail, sportId, pin))
                .enqueue(new Callback<ApiMessageResponse>() {
                    @Override
                    public void onResponse(Call<ApiMessageResponse> call,
                                           Response<ApiMessageResponse> response) {
                        Toast.makeText(
                                EmailVerificationActivity.this,
                                "Email verified successfully",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        Toast.makeText(
                                EmailVerificationActivity.this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}