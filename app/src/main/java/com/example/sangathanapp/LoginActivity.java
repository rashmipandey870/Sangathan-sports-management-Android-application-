/*package com.example.sangathanapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}*/

package com.example.sangathanapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.ui.captain.network.CaptainLoginApi;
import com.example.sangathanapp.ui.captain.network.CaptainLoginRequest;
import com.example.sangathanapp.ui.captain.network.CaptainLoginResponse;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.*;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView roleTitle;
    private View layoutSport;
    private AutoCompleteTextView sportDropdown;

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) userRole = "student";

        initViews();
        setupUIForRole();
        setupLoginAction();
    }

    private void initViews() {
        usernameInput = findViewById(R.id.login_id_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        roleTitle = findViewById(R.id.role_title);
        layoutSport = findViewById(R.id.layout_login_sport);
        sportDropdown = findViewById(R.id.login_sport_dropdown);
    }

    private void setupUIForRole() {

        roleTitle.setText("Login as " + userRole.toUpperCase());
        loginButton.setText("LOGIN AS " + userRole.toUpperCase());

        if (userRole.equalsIgnoreCase("captain")) {

            usernameInput.setHint("Team Name (e.g. BCA BOYS CRICKET)");
            passwordInput.setHint("Enter PIN");
            passwordInput.setVisibility(View.VISIBLE);
            layoutSport.setVisibility(View.GONE);

        } else {

            usernameInput.setHint("Username");
            passwordInput.setVisibility(View.VISIBLE);
            if (userRole.equalsIgnoreCase("coordinator")) {
                layoutSport.setVisibility(View.VISIBLE);
                loadSportsForLogin();
            } else {
                layoutSport.setVisibility(View.GONE);
            }
        }
    }

    private void loadSportsForLogin() {
        SportApi api = ApiClient.getClient().create(SportApi.class);
        api.getSports(null).enqueue(new Callback<List<SportModel>>() {
            @Override
            public void onResponse(Call<List<SportModel>> call, Response<List<SportModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> names = new ArrayList<>();
                    for (SportModel s : response.body()) {
                        names.add(s.getName().toUpperCase());
                    }
                    sportDropdown.setAdapter(new ArrayAdapter<>(
                            LoginActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            names
                    ));
                } else {
                    Toast.makeText(LoginActivity.this, "No sports available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SportModel>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed to load sports: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLoginAction() {

        loginButton.setOnClickListener(v -> {

            if (userRole.equalsIgnoreCase("captain")) {

                String teamName = usernameInput.getText().toString().trim();
                String pin = passwordInput.getText().toString().trim();

                if (teamName.isEmpty()) {
                    usernameInput.setError("Enter team name");
                    return;
                }

                if (pin.isEmpty()) {
                    passwordInput.setError("Enter PIN");
                    return;
                }

                loginCaptain(teamName, pin);

            } else {

                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameInput.setError("Required");
                    return;
                }

                if (password.isEmpty()) {
                    passwordInput.setError("Required");
                    return;
                }

                SharedPreferences.Editor editor = getSharedPreferences("APP", MODE_PRIVATE).edit();
                editor.putString("STUDENT_USERNAME", username);
                editor.putString("COORDINATOR_EMAIL", username);

                if (userRole.equalsIgnoreCase("coordinator")) {
                    String selectedSport = sportDropdown.getText().toString().trim();
                    if (selectedSport.isEmpty()) {
                        sportDropdown.setError("Assigned Sport is required");
                        Toast.makeText(LoginActivity.this, "Please select your assigned sport", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editor.putString("COORDINATOR_SPORT", selectedSport);
                } else {
                    editor.remove("COORDINATOR_SPORT");
                }

                editor.apply();
                redirectUser();
            }
        });
    }


    //  CAPTAIN LOGIN (REAL API)

    private void loginCaptain(String teamName, String pin) {

        CaptainLoginApi api = ApiClient.getClient().create(CaptainLoginApi.class);

        CaptainLoginRequest request = new CaptainLoginRequest(teamName, pin);

        api.captainlogin(request).enqueue(new Callback<CaptainLoginResponse>() {

            @Override
            public void onResponse(Call<CaptainLoginResponse> call,
                                   Response<CaptainLoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(
                            LoginActivity.this,
                            "Welcome Captain",
                            Toast.LENGTH_SHORT
                    ).show();

                    //  SAVE TEAM NAME AND ID FOR FUTURE USE
                    String teamId = response.body().getTeam().getId();
                    getSharedPreferences("APP", MODE_PRIVATE)
                            .edit()
                            .putString("TEAM_NAME", teamName)
                            .putString("TEAM_ID", teamId)
                            .apply();

                    redirectUser();

                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Invalid Team or PIN",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<CaptainLoginResponse> call, Throwable t) {

                Toast.makeText(
                        LoginActivity.this,
                        "Server error: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void redirectUser() {

        Intent intent;

        switch (userRole.toLowerCase()) {

            case "coordinator":
                intent = new Intent(this,
                        com.example.sangathanapp.ui.coordinator.CoordinatorDashboardActivity.class);
                break;

            case "captain":
                intent = new Intent(this,
                        com.example.sangathanapp.ui.captain.CaptainDashboardActivity.class);
                break;

            case "headadmin":
                intent = new Intent(this,
                        com.example.sangathanapp.ui.headadmin.HeadAdminDashboardActivity.class);
                break;

            default:
                intent = new Intent(this,
                        DashboardActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}