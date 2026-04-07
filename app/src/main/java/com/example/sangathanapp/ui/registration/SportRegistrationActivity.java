package com.example.sangathanapp.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.network.StudentRegistrationApi;
import com.example.sangathanapp.ui.registration.network.TeamApi;
import com.example.sangathanapp.ui.registration.network.TeamModel;
import com.example.sangathanapp.ui.trial.StudentTrialActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportRegistrationActivity extends AppCompatActivity {

    private TextInputEditText etName, etEnroll, etPhone, etEmail;
    private MaterialAutoCompleteTextView deptDropdown, genderDropdown, teamDropdown;
    private Button btnRegister;

    private List<TeamModel> allTeams = new ArrayList<>();
    private SportModel sport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_registration);

        initViews();
        setupDropdowns();
        loadDepartments();

        //  GET SPORT DATA FROM PREVIOUS ACTIVITY

        sport = getIntent().getParcelableExtra("SPORT_DATA");

        btnRegister.setOnClickListener(v -> submitRegistration());
    }

    // ================= INIT =================
    private void initViews() {
        etName = findViewById(R.id.student_name_input);
        etEnroll = findViewById(R.id.enrollment_no_input);
        etPhone = findViewById(R.id.phone_input);
        etEmail = findViewById(R.id.email_input);

        deptDropdown = findViewById(R.id.department_dropdown);
        genderDropdown = findViewById(R.id.gender_dropdown);
        teamDropdown = findViewById(R.id.team_dropdown);

        btnRegister = findViewById(R.id.btn_register);
    }

    // ================= DROPDOWNS =================
    private void setupDropdowns() {

        //  LOAD DEPARTMENTS FROM BACKEND
        ApiClient.getClient()
                .create(TeamApi.class)
                .getDepartments()
                .enqueue(new Callback<List<String>>() {

                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            deptDropdown.setAdapter(new ArrayAdapter<>(
                                    SportRegistrationActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    response.body()
                            ));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(SportRegistrationActivity.this,
                                "Failed to load departments",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Gender dropdown
        String[] genders = {"BOYS", "GIRLS"};
        genderDropdown.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genders
        ));

        //  LOAD TEAMS WHEN BOTH SELECTED
        deptDropdown.setOnItemClickListener((a, b, c, d) -> loadTeams());
        genderDropdown.setOnItemClickListener((a, b, c, d) -> loadTeams());
    }

    private void loadDepartments() {

        ApiClient.getClient()
                .create(TeamApi.class)
                .getDepartments()
                .enqueue(new Callback<List<String>>() {

                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            List<String> departments = response.body();

                            deptDropdown.setAdapter(new ArrayAdapter<>(
                                    SportRegistrationActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    departments
                            ));

                        } else {
                            Toast.makeText(SportRegistrationActivity.this,
                                    "No departments found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                        Toast.makeText(SportRegistrationActivity.this,
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ================= LOAD TEAMS =================
    private void loadTeams() {

        String dept = deptDropdown.getText().toString().trim();
        String gender = genderDropdown.getText().toString().trim();

        if (dept.isEmpty() || gender.isEmpty() || sport == null) return;

        ApiClient.getClient()
                .create(TeamApi.class)
                .getFilteredTeams(
                        dept.toUpperCase(),
                        gender.toUpperCase(),
                        sport.getName().toUpperCase()
                )
                .enqueue(new Callback<List<TeamModel>>() {

                    @Override
                    public void onResponse(Call<List<TeamModel>> call,
                                           Response<List<TeamModel>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            allTeams = response.body();

                            List<String> names = new ArrayList<>();

                            for (TeamModel t : allTeams) {
                                names.add(t.getTeamName());
                            }

                            teamDropdown.setAdapter(new ArrayAdapter<>(
                                    SportRegistrationActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    names
                            ));

                        } else {
                            Toast.makeText(SportRegistrationActivity.this,
                                    "No teams found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TeamModel>> call, Throwable t) {

                        Toast.makeText(SportRegistrationActivity.this,
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // ================= REGISTER =================
    private void submitRegistration() {

        String name = etName.getText().toString().trim().toUpperCase();
        String enroll = etEnroll.getText().toString().trim().toUpperCase();
        String dept = deptDropdown.getText().toString().trim().toUpperCase();
        String gender = genderDropdown.getText().toString().trim().toUpperCase();
        String team = teamDropdown.getText().toString().trim().toUpperCase();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim().toLowerCase();

        //  VALIDATION
        if (name.isEmpty() || enroll.isEmpty() || dept.isEmpty()
                || gender.isEmpty() || team.isEmpty()
                || phone.isEmpty() || email.isEmpty()) {

            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //  CREATE REQUEST MODEL
        StudentRegistrationModel model = new StudentRegistrationModel(
                UUID.randomUUID().toString(),
                name,
                enroll,
                dept,
                team,
                gender,
                phone,
                email,
                sport.getId(),

                "REGISTERED"
        );

        btnRegister.setEnabled(false);

        ApiClient.getClient()
                .create(StudentRegistrationApi.class)
                .registerStudent(model)
                .enqueue(new Callback<StudentRegistrationModel>() {

                    
                    @Override
                    public void onResponse(Call<StudentRegistrationModel> call,
                                           Response<StudentRegistrationModel> response) {

                        btnRegister.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            getSharedPreferences("APP", MODE_PRIVATE).edit().putString("TEAM_NAME",team).apply();

                            Toast.makeText(
                                    SportRegistrationActivity.this,
                                    "Registration Successful ",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(
                                    SportRegistrationActivity.this,
                                    StudentTrialActivity.class
                            );

                            intent.putExtra("SPORT_ID", sport.getId());
                            intent.putExtra("SPORT_NAME", sport.getName());
                            intent.putExtra("USER_EMAIL", email);

                            startActivity(intent);
                            finish();

                        } else {

                            try {
                                String errorMsg = response.errorBody().string();

                                Toast.makeText(
                                        SportRegistrationActivity.this,
                                        "Error: " + errorMsg,
                                        Toast.LENGTH_LONG
                                ).show();

                            } catch (Exception e) {
                                Toast.makeText(
                                        SportRegistrationActivity.this,
                                        "Unknown error",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<StudentRegistrationModel> call, Throwable t) {

                        btnRegister.setEnabled(true);

                        Toast.makeText(SportRegistrationActivity.this,
                                "Server error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}