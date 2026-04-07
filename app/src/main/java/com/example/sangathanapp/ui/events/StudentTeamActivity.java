package com.example.sangathanapp.ui.events;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class StudentTeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvCaptain;

    private StudentAdapter adapter;

    private String teamName = "BTECH"; //  replace dynamically later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_team);

        recyclerView = findViewById(R.id.recycler_team_players);
        tvCaptain = findViewById(R.id.tv_captain_name);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTeam();
    }

    private void loadTeam() {

        StudentApiService api =
                ApiClient.getClient().create(StudentApiService.class);

        api.getStudentTeam(teamName)
                .enqueue(new Callback<TeamViewResponse>() {

                    @Override
                    public void onResponse(
                            Call<TeamViewResponse> call,
                            Response<TeamViewResponse> response
                    ) {

                        if (response.isSuccessful() && response.body() != null) {

                            tvCaptain.setText(
                                    "Captain: " +
                                            response.body().getCaptainName()
                            );

                            adapter = new StudentAdapter(
                                    response.body().getPlayers(),"STUDENT"
                            );

                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<TeamViewResponse> call,
                            Throwable t
                    ) {
                        Toast.makeText(
                                StudentTeamActivity.this,
                                "Error loading team",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}