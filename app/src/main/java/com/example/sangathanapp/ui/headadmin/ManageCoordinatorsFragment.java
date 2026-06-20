package com.example.sangathanapp.ui.headadmin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCoordinatorsFragment extends Fragment {

    private EditText etYear;
    private Button btnFilter, btnAddSport;
    private RecyclerView recyclerView;
    private ManageSportsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_coordinators, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etYear = view.findViewById(R.id.et_sports_year);
        btnFilter = view.findViewById(R.id.btn_filter_sports);
        btnAddSport = view.findViewById(R.id.btn_add_sport);
        recyclerView = view.findViewById(R.id.recycler_coordinators);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnFilter.setOnClickListener(v -> filterSports());
        btnAddSport.setOnClickListener(v -> showAddSportDialog());

        // Default load for 2025
        fetchSports(2025);
    }

    private void filterSports() {
        String yearStr = etYear.getText().toString().trim();
        if (yearStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a year", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            fetchSports(year);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid year format", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSports(int year) {
        SportApi api = ApiClient.getClient().create(SportApi.class);
        api.getSports(year).enqueue(new Callback<List<SportModel>>() {
            @Override
            public void onResponse(Call<List<SportModel>> call, Response<List<SportModel>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    List<SportModel> sports = response.body();
                    adapter = new ManageSportsAdapter(sports, () -> fetchSports(year));
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No sports found for " + year, Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new ManageSportsAdapter(new ArrayList<>(), null));
                }
            }

            @Override
            public void onFailure(Call<List<SportModel>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Failed to load sports: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddSportDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_sport, null);
        EditText etName = view.findViewById(R.id.et_dialog_sport_name);
        EditText etDesc = view.findViewById(R.id.et_dialog_sport_desc);
        EditText etDate = view.findViewById(R.id.et_dialog_sport_date);
        EditText etVenue = view.findViewById(R.id.et_dialog_sport_venue);
        EditText etLevel = view.findViewById(R.id.et_dialog_sport_level);
        EditText etYearVal = view.findViewById(R.id.et_dialog_sport_year);
        EditText etMax = view.findViewById(R.id.et_dialog_sport_max);
        EditText etIconName = view.findViewById(R.id.et_dialog_sport_icon);

        // Prepopulate current year from filter
        etYearVal.setText(etYear.getText().toString().trim());

        new AlertDialog.Builder(getContext())
                .setTitle("Add New Sport")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();
                    String date = etDate.getText().toString().trim();
                    String venue = etVenue.getText().toString().trim();
                    String level = etLevel.getText().toString().trim();
                    String yearStr = etYearVal.getText().toString().trim();
                    String maxStr = etMax.getText().toString().trim();
                    String iconName = etIconName.getText().toString().trim();

                    if (name.isEmpty() || yearStr.isEmpty()) {
                        Toast.makeText(getContext(), "Sport name and year are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int year = Integer.parseInt(yearStr);
                    int maxParticipants = maxStr.isEmpty() ? 200 : Integer.parseInt(maxStr);
                    if (iconName.isEmpty()) iconName = "ic_default";

                    SportModel sport = new SportModel();
                    sport.setName(name);
                    sport.setDescription(desc);
                    sport.setDate(date);
                    sport.setVenue(venue);
                    sport.setLevel(level);
                    sport.setYear(year);
                    sport.setMaxPlayers(maxParticipants);
                    sport.setIconName(iconName);

                    SportApi api = ApiClient.getClient().create(SportApi.class);
                    api.addSport(sport).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Sport Added Successfully", Toast.LENGTH_SHORT).show();
                                fetchSports(year);
                            } else {
                                String error = "Failed to add sport";
                                try {
                                    if (response.errorBody() != null) {
                                        error = new org.json.JSONObject(response.errorBody().string()).getString("message");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}