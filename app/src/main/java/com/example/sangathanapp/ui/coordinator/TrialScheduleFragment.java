package com.example.sangathanapp.ui.coordinator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.TrialApi;
import com.example.sangathanapp.ui.coordinator.network.TrialRequest;
import com.example.sangathanapp.ui.registration.DummySportRepository;
import com.example.sangathanapp.ui.registration.SportModel;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrialScheduleFragment extends Fragment {

    private int selectedSportId = -1;
    private String selectedSportName = "";
    String teamName;

    private TextView tvSelectedSport;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_coordinator_trial_schedule,
                container,
                false
        );

        tvSelectedSport = view.findViewById(R.id.tv_selected_sport);

        // ---------------- SPORT GRID ----------------
        RecyclerView recycler = view.findViewById(R.id.recycler_sports);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        CoordinatorSportAdapter adapter =
                new CoordinatorSportAdapter(
                        DummySportRepository.getInstance().getAllSports(),
                        sportId -> {
                            selectedSportId = sportId;
                            SportModel sport =
                                    DummySportRepository.getInstance()
                                            .getSportById(sportId);

                            if (sport != null) {
                                selectedSportName = sport.getName();
                                tvSelectedSport.setText(
                                        "Selected Sport: " + selectedSportName
                                );
                            }
                        }
                );

        recycler.setAdapter(adapter);


        TextInputEditText etDate = view.findViewById(R.id.input_trial_date);
        TextInputEditText etTime = view.findViewById(R.id.input_trial_time);
        TextInputEditText etVenue = view.findViewById(R.id.input_trial_venue);
        Button btnSave = view.findViewById(R.id.btn_save_schedule);

        btnSave.setOnClickListener(v -> {

            if (selectedSportId == -1) {
                Toast.makeText(
                        getContext(),
                        "Please select a sport",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String venue = etVenue.getText().toString().trim();

            if (date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
                Toast.makeText(
                        getContext(),
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            saveTrialSchedule(
                    selectedSportId,
                    selectedSportName,
                    date,
                    time,
                    venue
            );
        });

        return view;
    }

    // ---------------- API CALL ----------------
    private void saveTrialSchedule(
            int sportId,
            String sportName,
            String date,
            String time,
            String venue
    ) {

        TrialApi api =
                ApiClient.getClient().create(TrialApi.class);

        TrialRequest request =
                new TrialRequest(
                        String.valueOf(sportId),
                        teamName,
                        sportName,
                        date,
                        time,
                        venue
                );

        api.createTrial(request).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(
                            getContext(),
                            response.body().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    try {
                        Log.e("TRIAL_API", "Code: " + response.code());
                        Log.e("TRIAL_API", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(
                            getContext(),
                            "Failed to save schedule",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ApiResponse> call,
                    Throwable t
            ) {
                Toast.makeText(
                        getContext(),
                        "Server error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}