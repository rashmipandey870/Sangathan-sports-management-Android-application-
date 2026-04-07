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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTeamFragment extends Fragment {

    private RecyclerView recyclerView;
    private String teamName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_team, container, false);

        recyclerView = v.findViewById(R.id.recycler_team);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        teamName = requireContext()
                .getSharedPreferences("APP", 0)
                .getString("TEAM_NAME", "");

        loadTeam();

        return v;
    }

    private void loadTeam() {

        StudentApiService api =
                ApiClient.getClient().create(StudentApiService.class);

        api.getStudentTeam(teamName)
                .enqueue(new Callback<TeamViewResponse>() {

                    @Override
                    public void onResponse(Call<TeamViewResponse> call,
                                           Response<TeamViewResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

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