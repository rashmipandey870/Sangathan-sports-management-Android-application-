package com.example.sangathanapp.ui;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.StudentAdapter;
import com.example.sangathanapp.ui.coordinator.network.StudentApiService;
import com.example.sangathanapp.ui.coordinator.network.TeamViewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTeamFragment extends Fragment {

    private RecyclerView recyclerView;
    private android.widget.TextView tvCaptainName;
    private String teamName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_team, container, false);

        recyclerView = v.findViewById(R.id.recycler_team);
        tvCaptainName = v.findViewById(R.id.tv_team_captain_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        teamName = requireContext()
                .getSharedPreferences("APP", 0)
                .getString("TEAM_NAME", "");

        checkSelectionAndLoad();

        return v;
    }

    private void checkSelectionAndLoad() {
        String username = requireContext()
                .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                .getString("STUDENT_USERNAME", "");

        if (username.isEmpty() || teamName.isEmpty()) {
            loadTeam();
            return;
        }

        com.example.sangathanapp.ui.registration.network.StudentRegistrationApi api =
                ApiClient.getClient().create(com.example.sangathanapp.ui.registration.network.StudentRegistrationApi.class);

        api.getStudentStatus(username).enqueue(new Callback<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>>() {
            @Override
            public void onResponse(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call,
                                   Response<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> response) {
                if (!isAdded()) return;

                boolean isSelected = false;
                if (response.isSuccessful() && response.body() != null) {
                    for (com.example.sangathanapp.ui.registration.StudentRegistrationModel r : response.body()) {
                        if (teamName.equalsIgnoreCase(r.getTeamName()) && "SELECTED".equalsIgnoreCase(r.getStatus())) {
                            isSelected = true;
                            break;
                        }
                    }
                }

                if (isSelected) {
                    loadTeam();
                } else {
                    tvCaptainName.setText("Status: Pending Selection");
                    tvCaptainName.setTextColor(0xFFFFD700);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call, Throwable t) {
                if (!isAdded()) return;
                String localStatus = requireContext()
                        .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                        .getString("SELECTION_STATUS_FOR_" + teamName, "");
                
                if ("SELECTED".equalsIgnoreCase(localStatus)) {
                    loadTeam();
                } else {
                    tvCaptainName.setText("Status: Pending Selection (Offline)");
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadTeam() {
        recyclerView.setVisibility(View.VISIBLE);

        StudentApiService api =
                ApiClient.getClient().create(StudentApiService.class);

        api.getStudentTeam(teamName)
                .enqueue(new Callback<TeamViewResponse>() {

                    @Override
                    public void onResponse(Call<TeamViewResponse> call,
                                           Response<TeamViewResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            String captain = response.body().getCaptainName();
                            if (captain != null && !captain.isEmpty()) {
                                tvCaptainName.setText("Captain: " + captain);
                            } else {
                                tvCaptainName.setText("Captain: Not Assigned");
                            }

                            recyclerView.setAdapter(
                                    new StudentAdapter(
                                            response.body().getPlayers(),
                                            "STUDENT"
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamViewResponse> call, Throwable t) {

                        Toast.makeText(getContext(),
                                "Failed to load team",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}