package com.example.sangathanapp.ui.headadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.headadmin.network.HeadAdminApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOverviewFragment extends Fragment {

    private TextView tvSports, tvTeams, tvStudents, tvFixtures;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSports = view.findViewById(R.id.tv_stat_sports);
        tvTeams = view.findViewById(R.id.tv_stat_teams);
        tvStudents = view.findViewById(R.id.tv_stat_students);
        tvFixtures = view.findViewById(R.id.tv_stat_fixtures);

        loadStats();
    }

    private void loadStats() {
        HeadAdminApi api = ApiClient.getClient().create(HeadAdminApi.class);
        api.getGeneralStats().enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> stats = response.body();
                    
                    if (stats.containsKey("totalSports")) {
                        tvSports.setText(String.valueOf(stats.get("totalSports")));
                    }
                    if (stats.containsKey("totalTeams")) {
                        tvTeams.setText(String.valueOf(stats.get("totalTeams")));
                    }
                    if (stats.containsKey("totalStudents")) {
                        tvStudents.setText(String.valueOf(stats.get("totalStudents")));
                    }
                    if (stats.containsKey("totalFixtures")) {
                        tvFixtures.setText(String.valueOf(stats.get("totalFixtures")));
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load dashboard statistics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e("OVERVIEW_DEBUG", "Stats API failed: " + t.getMessage());
            }
        });
    }
}