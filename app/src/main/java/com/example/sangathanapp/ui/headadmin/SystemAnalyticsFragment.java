package com.example.sangathanapp.ui.headadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.headadmin.network.HeadAdminApi;
import com.example.sangathanapp.ui.headadmin.network.LeaderboardEntry;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.LinearLayout;
import android.widget.TextView;

public class SystemAnalyticsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private MaterialButton btnBoys, btnGirls;
    private Spinner sportSpinner;
    private String selectedGender = "BOYS";
    private String selectedSport = "";
    
    private LinearLayout chartContainer;
    private View cardChart;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_system_analytics,
                 container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBoys = view.findViewById(R.id.btn_leaderboard_boys);
        btnGirls = view.findViewById(R.id.btn_leaderboard_girls);
        sportSpinner = view.findViewById(R.id.sp_leaderboard_sport);
        
        chartContainer = view.findViewById(R.id.layout_chart_container);
        cardChart = view.findViewById(R.id.card_leaderboard_chart);

        setupSportSpinner();
        setupGenderToggle();
    }

    private void setupSportSpinner() {
        SportApi api = ApiClient.getClient().create(SportApi.class);
        api.getSports(null).enqueue(new Callback<List<SportModel>>() {
            @Override
            public void onResponse(Call<List<SportModel>> call, Response<List<SportModel>> response) {
                if (!isAdded()) return;
                List<String> sports = new ArrayList<>();
                sports.add("ALL SPORTS");
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    for (SportModel s : response.body()) {
                        sports.add(s.getName().toLowerCase());
                    }
                } else {
                    sports.add("cricket");
                    sports.add("football");
                    sports.add("badminton");
                    sports.add("basketball");
                    sports.add("volleyball");
                    sports.add("carramboard");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        sports
                );
                sportSpinner.setAdapter(adapter);

                sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selection = sports.get(position);
                        if ("ALL SPORTS".equalsIgnoreCase(selection)) {
                            selectedSport = "";
                        } else {
                            selectedSport = selection;
                        }
                        loadLeaderboard(selectedGender, selectedSport);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onFailure(Call<List<SportModel>> call, Throwable t) {
                if (!isAdded()) return;
                List<String> sports = new ArrayList<>();
                sports.add("ALL SPORTS");
                sports.add("cricket");
                sports.add("football");
                sports.add("badminton");
                sports.add("basketball");
                sports.add("volleyball");
                sports.add("carramboard");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        sports
                );
                sportSpinner.setAdapter(adapter);

                sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selection = sports.get(position);
                        if ("ALL SPORTS".equalsIgnoreCase(selection)) {
                            selectedSport = "";
                        } else {
                            selectedSport = selection;
                        }
                        loadLeaderboard(selectedGender, selectedSport);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });
    }

    private void setupGenderToggle() {
        btnBoys.setOnClickListener(v -> {
            selectedGender = "BOYS";
            btnBoys.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1E88E5));
            btnBoys.setTextColor(0xFFFFFFFF);

            btnGirls.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF131C2D));
            btnGirls.setTextColor(0xFF8A9CB4);

            loadLeaderboard(selectedGender, selectedSport);
        });

        btnGirls.setOnClickListener(v -> {
            selectedGender = "GIRLS";
            btnGirls.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1E88E5));
            btnGirls.setTextColor(0xFFFFFFFF);

            btnBoys.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF131C2D));
            btnBoys.setTextColor(0xFF8A9CB4);

            loadLeaderboard(selectedGender, selectedSport);
        });
    }

    private void loadLeaderboard(String gender, String sport) {
        HeadAdminApi api = ApiClient.getClient().create(HeadAdminApi.class);
        api.getLeaderboard(gender, sport.isEmpty() ? null : sport).enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    List<LeaderboardEntry> list = response.body();
                    adapter = new LeaderboardAdapter(list);
                    recyclerView.setAdapter(adapter);
                    updateChart(list);
                } else {
                    loadPlaceholderData(gender);
                }
            }

            @Override
            public void onFailure(Call<List<LeaderboardEntry>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e("ANALYTICS_DEBUG", "Leaderboard API failed: " + t.getMessage());
                loadPlaceholderData(gender);
            }
        });
    }

    private void loadPlaceholderData(String gender) {
        List<LeaderboardEntry> placeholders = new ArrayList<>();
        if ("GIRLS".equalsIgnoreCase(gender)) {
            placeholders.add(new LeaderboardEntry("INFORMATION TECHNOLOGY", 16));
            placeholders.add(new LeaderboardEntry("COMPUTER SCIENCE & ENGINEERING", 13));
            placeholders.add(new LeaderboardEntry("BUSINESS ADMINISTRATION", 11));
            placeholders.add(new LeaderboardEntry("ELECTRONICS & COMMUNICATION", 9));
            placeholders.add(new LeaderboardEntry("CIVIL ENGINEERING", 4));
            placeholders.add(new LeaderboardEntry("MECHANICAL ENGINEERING", 2));
        } else {
            placeholders.add(new LeaderboardEntry("COMPUTER SCIENCE & ENGINEERING", 18));
            placeholders.add(new LeaderboardEntry("INFORMATION TECHNOLOGY", 14));
            placeholders.add(new LeaderboardEntry("ELECTRONICS & COMMUNICATION", 11));
            placeholders.add(new LeaderboardEntry("MECHANICAL ENGINEERING", 7));
            placeholders.add(new LeaderboardEntry("CIVIL ENGINEERING", 5));
            placeholders.add(new LeaderboardEntry("BUSINESS ADMINISTRATION", 3));
        }
        
        adapter = new LeaderboardAdapter(placeholders);
        recyclerView.setAdapter(adapter);
        updateChart(placeholders);
    }

    private void updateChart(List<LeaderboardEntry> list) {
        if (chartContainer == null || cardChart == null) return;
        chartContainer.removeAllViews();
        if (list == null || list.isEmpty()) {
            cardChart.setVisibility(View.GONE);
            return;
        }
        cardChart.setVisibility(View.VISIBLE);

        // Find max wins to calculate percentage
        int maxWins = 0;
        for (LeaderboardEntry entry : list) {
            if (entry.getWins() > maxWins) {
                maxWins = entry.getWins();
            }
        }
        if (maxWins == 0) maxWins = 1; // Avoid division by zero

        for (int i = 0; i < list.size(); i++) {
            LeaderboardEntry entry = list.get(i);
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_leaderboard_chart_row, chartContainer, false);
            
            TextView tvName = rowView.findViewById(R.id.tv_chart_dept_name);
            TextView tvWins = rowView.findViewById(R.id.tv_chart_wins_count);
            View viewProgress = rowView.findViewById(R.id.view_chart_progress);
            View viewSpacer = rowView.findViewById(R.id.view_chart_spacer);

            tvName.setText(entry.getDepartment());
            tvWins.setText(entry.getWins() + " Wins");

            // Calculate percentage width (min 6% for visual representation if 0 wins, max 100%)
            int percent = (entry.getWins() * 100) / maxWins;
            if (percent < 6) percent = 6;

            // Highlight top ranking department in Gold gradient
            if (i == 0 && entry.getWins() > 0) {
                viewProgress.setBackgroundResource(R.drawable.bg_chart_bar_fill_gold);
                tvWins.setTextColor(0xFFFFD700); // Gold color
            } else {
                viewProgress.setBackgroundResource(R.drawable.bg_chart_bar_fill);
                tvWins.setTextColor(0xFF00E5FF); // Neon cyan
            }

            LinearLayout.LayoutParams progressParams = (LinearLayout.LayoutParams) viewProgress.getLayoutParams();
            progressParams.weight = percent;
            viewProgress.setLayoutParams(progressParams);

            LinearLayout.LayoutParams spacerParams = (LinearLayout.LayoutParams) viewSpacer.getLayoutParams();
            spacerParams.weight = 100 - percent;
            viewSpacer.setLayoutParams(spacerParams);

            chartContainer.addView(rowView);
        }
    }
}