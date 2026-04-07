package com.example.sangathanapp.ui.captain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.captain.network.CaptainLoginApi;
import com.example.sangathanapp.ui.captain.network.CaptainLoginRequest;
import com.example.sangathanapp.ui.captain.network.CaptainLoginResponse;

import retrofit2.*;

public class CaptainLoginActivity extends AppCompatActivity {

    private EditText etTeamName, etPin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_login);

        etTeamName = findViewById(R.id.et_team_name);
        etPin = findViewById(R.id.et_pin_captain);
        btnLogin = findViewById(R.id.btn_login_captain);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {

        String teamName = etTeamName.getText().toString().trim();
        String pin = etPin.getText().toString().trim();

        if (teamName.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        CaptainLoginApi api = ApiClient.getClient().create(CaptainLoginApi.class);
        CaptainLoginRequest request = new CaptainLoginRequest(teamName, pin);

        api.captainlogin(request).enqueue(new Callback<CaptainLoginResponse>() {

            @Override
            public void onResponse(Call<CaptainLoginResponse> call,
                                   Response<CaptainLoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    String teamId = response.body().getTeam().getId();
                    String teamName = response.body().getTeam().getTeamName();

                    Log.d("LOGIN_DEBUG", "TEAM_ID FROM API: " + teamId);

                    //  SAVE TEAM ID
                    getSharedPreferences("APP", MODE_PRIVATE)
                            .edit()
                            .putString("TEAM_ID", teamId)
                            .putString("TEAM_NAME", teamName)
                            .commit();

                    // VERIFY SAVE
                    String saved = getSharedPreferences("APP", MODE_PRIVATE)
                            .getString("TEAM_ID", "NOT_FOUND");

                    Log.d("LOGIN_DEBUG", "TEAM_ID AFTER SAVE: " + saved);

                    Toast.makeText(
                            CaptainLoginActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent i = new Intent(
                            CaptainLoginActivity.this,
                            CaptainDashboardActivity.class
                    );
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(
                            CaptainLoginActivity.this,
                            "Invalid Team or PIN",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<CaptainLoginResponse> call, Throwable t) {
                Toast.makeText(
                        CaptainLoginActivity.this,
                        "Server Error",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}