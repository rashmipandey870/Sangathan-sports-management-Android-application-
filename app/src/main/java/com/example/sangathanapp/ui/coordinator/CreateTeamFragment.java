package com.example.sangathanapp.ui.coordinator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.CreateTeamRequest;
import com.example.sangathanapp.ui.coordinator.network.CreateTeamResponse;
import com.example.sangathanapp.ui.coordinator.network.TeamApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTeamFragment extends Fragment {

    private EditText etTeamName, etCaptainName, etEnrollment;
    private Spinner spGender, spSport, spDepartment;
    private Button btnCreateTeam;
    private TextView tvGeneratedPin;
    private TextView tvFixedSport, tvSportLabel;
    private String assignedSport = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        etTeamName = view.findViewById(R.id.et_team_name);
        etCaptainName = view.findViewById(R.id.et_captain_name);
        etEnrollment = view.findViewById(R.id.et_captain_enrollment);
        spDepartment = view.findViewById(R.id.sp_captain_department);

        spGender = view.findViewById(R.id.sp_gender);
        spSport = view.findViewById(R.id.sp_sport);
        tvFixedSport = view.findViewById(R.id.tv_fixed_sport);
        tvSportLabel = view.findViewById(R.id.tv_sport_label);

        btnCreateTeam = view.findViewById(R.id.btn_create_team);
        tvGeneratedPin = view.findViewById(R.id.tv_generated_pin);

        setupSpinners();
        checkCoordinatorSport(view);

        btnCreateTeam.setOnClickListener(v -> createTeam());
    }


    // SPINNER SETUP

    private void setupSpinners() {

        String[] genders = {"BOYS", "GIRLS"};
        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        genders);
        spGender.setAdapter(genderAdapter);

        String[] sports = {
                "CRICKET","FOOTBALL","BASKETBALL",
                "VOLLEYBALL","BADMINTON","KABADDI"
        };

        ArrayAdapter<String> sportAdapter =
                new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        sports);
        spSport.setAdapter(sportAdapter);

        String[] departments = {
                "COMPUTER SCIENCE & ENGINEERING",
                "INFORMATION TECHNOLOGY",
                "ELECTRONICS & COMMUNICATION",
                "MECHANICAL ENGINEERING",
                "CIVIL ENGINEERING",
                "BUSINESS ADMINISTRATION"
        };

        ArrayAdapter<String> deptAdapter =
                new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        departments);
        spDepartment.setAdapter(deptAdapter);
    }

    private void checkCoordinatorSport(View view) {
        SharedPreferences sp = requireContext().getSharedPreferences("APP", Context.MODE_PRIVATE);
        String cachedSport = sp.getString("COORDINATOR_SPORT", "");
        if (!TextUtils.isEmpty(cachedSport)) {
            assignedSport = cachedSport.trim();
            spSport.setVisibility(View.GONE);
            tvFixedSport.setText(assignedSport.toUpperCase());
            tvFixedSport.setVisibility(View.VISIBLE);
            tvSportLabel.setText("Assigned Sport");
        } else {
            String coordinatorEmail = sp.getString("COORDINATOR_EMAIL", "");
            if (!TextUtils.isEmpty(coordinatorEmail)) {
                SportApi sportApi = ApiClient.getClient().create(SportApi.class);
                sportApi.getCoordinatorSport(coordinatorEmail).enqueue(new Callback<SportModel>() {
                    @Override
                    public void onResponse(Call<SportModel> call, Response<SportModel> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            assignedSport = response.body().getName();
                            spSport.setVisibility(View.GONE);
                            tvFixedSport.setText(assignedSport.toUpperCase());
                            tvFixedSport.setVisibility(View.VISIBLE);
                            tvSportLabel.setText("Assigned Sport");
                        }
                    }
                    @Override
                    public void onFailure(Call<SportModel> call, Throwable t) {}
                });
            }
        }
    }


    // CREATE TEAM

    private void createTeam() {

        String teamName = etTeamName.getText().toString().trim();
        String captainName = etCaptainName.getText().toString().trim();
        String enrollment = etEnrollment.getText().toString().trim();
        String department = spDepartment.getSelectedItem().toString();
        String gender = spGender.getSelectedItem().toString();
        String sport = (assignedSport != null) ? assignedSport.toLowerCase() : spSport.getSelectedItem().toString().toLowerCase();

        if (TextUtils.isEmpty(teamName) ||
                TextUtils.isEmpty(captainName) ||
                TextUtils.isEmpty(enrollment)) {

            Toast.makeText(getContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        CreateTeamRequest request = new CreateTeamRequest(
                teamName, captainName, enrollment,
                department, gender, sport
        );

        TeamApi api = ApiClient.getClient().create(TeamApi.class);

        api.createTeam(request).enqueue(new Callback<CreateTeamResponse>() {

            @Override
            public void onResponse(Call<CreateTeamResponse> call,
                                   Response<CreateTeamResponse> response) {

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {

                    String pin = response.body().getPin();

                    //
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Team Created")
                            .setMessage("Captain PIN: " + pin)
                            .setPositiveButton("OK", null)
                            .show();

                    tvGeneratedPin.setText("PIN: " + pin);

                    clearFields(); //
                } else {
                    String errorMsg = "Failed to create team";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = new org.json.JSONObject(response.errorBody().string()).getString("message");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CreateTeamResponse> call, Throwable t) {

                if (!isAdded()) return;

                Toast.makeText(requireContext(),
                        "Server error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    // CLEAR FIELDS

    private void clearFields() {
        etTeamName.setText("");
        etCaptainName.setText("");
        etEnrollment.setText("");
        spDepartment.setSelection(0);
    }
}