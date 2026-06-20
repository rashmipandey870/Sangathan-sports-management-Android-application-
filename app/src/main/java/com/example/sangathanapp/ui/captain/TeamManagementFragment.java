package com.example.sangathanapp.ui.captain;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.StudentAdapter;
import com.example.sangathanapp.ui.coordinator.network.TeamApi;
import com.example.sangathanapp.ui.coordinator.network.TeamViewResponse;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.*;

public class TeamManagementFragment extends Fragment {

    private RecyclerView recyclerCandidates;
    private android.widget.TextView tvCaptainName;
    private String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        teamName=requireContext().getSharedPreferences("APP",0).getString("TEAM_NAME","");
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_team_management,
                container,
                false
        );

        recyclerCandidates = view.findViewById(R.id.recycler_candidates);
        tvCaptainName = view.findViewById(R.id.tv_team_captain_name);
        recyclerCandidates.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        loadPlayers();

        return view;
    }

    private void loadPlayers() {

        TeamApi api = ApiClient.getClient().create(TeamApi.class);

        api.getTeamPlayers(teamName,"SELECTED")
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

                            List<StudentRegistrationModel> players =
                                    response.body().getPlayers();

                            if (players.isEmpty()) {
                                Toast.makeText(getContext(),
                                        "No players found",
                                        Toast.LENGTH_SHORT).show();
                            }

                            recyclerCandidates.setAdapter(
                                    new StudentAdapter(players, "CAPTAIN_TEAM")
                                );
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamViewResponse> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "Failed to load players",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}