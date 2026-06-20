package com.example.sangathanapp.ui.trial;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.trial.network.StudentTrialApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTrialActivity extends AppCompatActivity {

    TextView tvDate, tvTime, tvVenue, tvInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_trial);

        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvVenue = findViewById(R.id.tv_venue);
        tvInstruction = findViewById(R.id.tv_instruction);

        loadTrial();
    }

    private void loadTrial() {

        String teamName = getIntent().getStringExtra("TEAM_NAME");
        if (teamName == null || teamName.isEmpty()) {
            teamName = getSharedPreferences("APP", MODE_PRIVATE).getString("TEAM_NAME", "");
        }

        Log.d("TRIAL_DEBUG", "TEAM_NAME: " + teamName);

        if (teamName.isEmpty()) {
            tvDate.setText("Team not found");
            return;
        }

        StudentTrialApi api = ApiClient.getClient().create(StudentTrialApi.class);

        api.getTrialByTeamName(teamName)
                .enqueue(new Callback<List<TrialScheduleModel>>() {

                    @Override
                    public void onResponse(Call<List<TrialScheduleModel>> call,
                                           Response<List<TrialScheduleModel>> response) {

                        Log.d("TRIAL_DEBUG", "CODE: " + response.code());

                        if (response.isSuccessful()
                                && response.body() != null
                                && !response.body().isEmpty()) {

                            TrialScheduleModel trial = response.body().get(0);

                            tvDate.setText(trial.getDate());
                            tvTime.setText(trial.getTime());
                            tvVenue.setText(trial.getVenue());
                            tvInstruction.setText(trial.getInstructions());

                        } else {

                            tvDate.setText("No Trial Scheduled");
                            tvTime.setText("-");
                            tvVenue.setText("-");
                            tvInstruction.setText("-");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TrialScheduleModel>> call, Throwable t) {

                        Log.d("TRIAL_DEBUG", "ERROR: " + t.getMessage());

                        tvDate.setText("Error loading trial");
                    }
                });
    }
}