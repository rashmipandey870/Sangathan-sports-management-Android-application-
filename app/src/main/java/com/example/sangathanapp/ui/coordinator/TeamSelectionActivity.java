package com.example.sangathanapp.ui.coordinator;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.StudentAdapter;
import com.example.sangathanapp.ui.coordinator.network.StudentApiService;
import com.example.sangathanapp.ui.coordinator.network.TeamApi;
import com.example.sangathanapp.ui.coordinator.network.TeamViewResponse;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.*;

public class TeamSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String teamName;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        recyclerView = findViewById(R.id.recycler_students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teamName = getIntent().getStringExtra("TEAM_NAME");
        role = getIntent().getStringExtra("ROLE");

        loadStudents();
    }

    private void loadStudents() {

        TeamApi api = ApiClient.getClient().create(TeamApi.class);

        api.getTeamPlayers(teamName,"SELECTED")
                .enqueue(new Callback<TeamViewResponse>() {

                    @Override
                    public void onResponse(
                            Call<TeamViewResponse> call,
                            Response<TeamViewResponse> response
                    ) {

                        if (response.isSuccessful() && response.body() != null) {

                            List<StudentRegistrationModel> players =
                                    response.body().getPlayers();

                            recyclerView.setAdapter(
                                    new StudentAdapter(players, role)
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<TeamViewResponse> call,
                            Throwable t
                    ) {
                        Toast.makeText(
                                TeamSelectionActivity.this,
                                "Failed to load players",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}