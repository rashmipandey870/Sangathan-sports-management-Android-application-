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
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;


import com.example.sangathanapp.ui.captain.network.CaptainLoginApi;
import com.example.sangathanapp.ui.captain.network.CaptainLoginRequest;
import com.example.sangathanapp.ui.captain.network.CaptainLoginResponse;


import retrofit2.*;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView roleTitle;

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    private void setupUIForRole() {

        roleTitle.setText("Login as " + userRole.toUpperCase());
        loginButton.setText("LOGIN AS " + userRole.toUpperCase());

        if (userRole.equalsIgnoreCase("captain")) {

            usernameInput.setHint("Team Name (e.g. BCA BOYS CRICKET)");
            passwordInput.setHint("Enter PIN");
            passwordInput.setVisibility(View.VISIBLE);

        } else {

            usernameInput.setHint("Username");
            passwordInput.setVisibility(View.VISIBLE);
        }
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

                    //  SAVE TEAM NAME FOR FUTURE USE
                    getSharedPreferences("APP", MODE_PRIVATE)
                            .edit()
                            .putString("TEAM_NAME", teamName)
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