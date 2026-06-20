package com.example.sangathanapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.FixtureApi;
import com.example.sangathanapp.ui.matches.FixtureModel;
import com.example.sangathanapp.ui.registration.DummySportRepository;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerSports;
    private SportAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerSports = view.findViewById(R.id.recycler_sports_home);
        recyclerSports.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Dynamically load sports for the current calendar year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        loadSports(currentYear);

        // Load Selection Status if user is logged in
        loadSelectionStatus(view);

        // Wire up Search EditText
        com.google.android.material.textfield.TextInputEditText etSearch = view.findViewById(R.id.et_search_sports);
        if (etSearch != null) {
            etSearch.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (adapter != null) {
                        adapter.filter(s.toString());
                    }
                }
                @Override
                public void afterTextChanged(android.text.Editable s) {}
            });
        }

        // Wire up Trial Info Card
        View cardTrial = view.findViewById(R.id.card_trial_info);
        if (cardTrial != null) {
            cardTrial.setOnClickListener(v -> {
                String teamName = requireContext()
                        .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                        .getString("TEAM_NAME", "");
                if (teamName.isEmpty()) {
                    new android.app.AlertDialog.Builder(requireContext())
                            .setTitle("No Active Registration")
                            .setMessage("You must first register for a sport team to view trial schedules.")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    android.content.Intent intent = new android.content.Intent(getContext(), com.example.sangathanapp.ui.trial.StudentTrialActivity.class);
                    intent.putExtra("TEAM_NAME", teamName);
                    startActivity(intent);
                }
            });
        }
    }

    private void loadSelectionStatus(View view) {
        androidx.cardview.widget.CardView cardSelection = view.findViewById(R.id.card_selection_status);
        android.widget.LinearLayout layoutItems = view.findViewById(R.id.layout_status_items);

        String username = requireContext()
                .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                .getString("STUDENT_USERNAME", "");

        if (username.isEmpty()) {
            cardSelection.setVisibility(View.GONE);
            return;
        }

        com.example.sangathanapp.ui.registration.network.StudentRegistrationApi api =
                ApiClient.getClient().create(com.example.sangathanapp.ui.registration.network.StudentRegistrationApi.class);

        api.getStudentStatus(username).enqueue(new Callback<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>>() {
            @Override
            public void onResponse(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call,
                                   Response<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    cardSelection.setVisibility(View.VISIBLE);
                    layoutItems.removeAllViews();

                    List<com.example.sangathanapp.ui.registration.StudentRegistrationModel> regs = response.body();

                    for (com.example.sangathanapp.ui.registration.StudentRegistrationModel r : regs) {
                        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_status_row, layoutItems, false);
                        android.widget.TextView tvName = item.findViewById(R.id.tv_status_team_name);
                        android.widget.TextView tvBadge = item.findViewById(R.id.tv_status_badge);

                        tvName.setText(r.getTeamName());
                        String status = r.getStatus();
                        tvBadge.setText(status);

                        View layoutHint = item.findViewById(R.id.layout_click_hint);
                        if ("REJECTED".equalsIgnoreCase(status)) {
                            if (layoutHint != null) layoutHint.setVisibility(View.GONE);
                        } else {
                            if (layoutHint != null) layoutHint.setVisibility(View.VISIBLE);
                            item.setOnClickListener(v -> {
                                android.content.Intent intent = new android.content.Intent(getContext(), com.example.sangathanapp.ui.trial.StudentTrialActivity.class);
                                intent.putExtra("TEAM_NAME", r.getTeamName());
                                startActivity(intent);
                            });
                        }

                        // Style badge based on status
                        if ("SELECTED".equalsIgnoreCase(status)) {
                            tvBadge.setTextColor(0xFF388E3C); // Green
                            tvBadge.setBackgroundColor(0x1A388E3C);
                            
                            // Check if notified
                            String notifyKey = "SELECTION_NOTIFIED_FOR_" + r.getTeamName();
                            boolean alreadyNotified = requireContext()
                                    .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                                    .getBoolean(notifyKey, false);

                            if (!alreadyNotified) {
                                showSelectionDialog(r.getTeamName());
                                requireContext()
                                        .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                                        .edit()
                                        .putBoolean(notifyKey, true)
                                        .apply();
                            }
                            
                            // Also save selection status to SharedPreferences so the team fragment can unlock
                            requireContext()
                                    .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("SELECTION_STATUS_FOR_" + r.getTeamName(), "SELECTED")
                                    .apply();
                        } else if ("REJECTED".equalsIgnoreCase(status)) {
                            tvBadge.setTextColor(0xFFD32F2F); // Red
                            tvBadge.setBackgroundColor(0x1AD32F2F);
                            
                            requireContext()
                                    .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("SELECTION_STATUS_FOR_" + r.getTeamName(), "REJECTED")
                                    .apply();
                        } else {
                            tvBadge.setTextColor(0xFFFFD700); // Gold
                            tvBadge.setBackgroundColor(0x1AFFD700);
                            
                            requireContext()
                                    .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("SELECTION_STATUS_FOR_" + r.getTeamName(), "REGISTERED")
                                    .apply();
                        }

                        layoutItems.addView(item);
                    }
                } else {
                    cardSelection.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call, Throwable t) {
                if (!isAdded()) return;
                cardSelection.setVisibility(View.GONE);
            }
        });
    }

    private void showSelectionDialog(String teamName) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("🎉 Congratulations!")
                .setMessage("You have been officially SELECTED for the team:\n\n" + teamName + "\n\nWelcome to the squad! Go to the 'Team' tab to view your teammates details.")
                .setPositiveButton("Awesome", null)
                .show();
    }



    private void loadSports(int year) {
        SportApi api = ApiClient.getClient().create(SportApi.class);
        api.getSports(year).enqueue(new Callback<List<SportModel>>() {

            @Override
            public void onResponse(Call<List<SportModel>> call, Response<List<SportModel>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<SportModel> sports = response.body();
                    adapter = new SportAdapter(sports);
                    recyclerSports.setAdapter(adapter);
                } else {
                    Log.d("HOME_DEBUG", "No sports found in API for year " + year + ". Using fallback offline repository.");
                    loadFallbackSports();
                }
            }

            @Override
            public void onFailure(Call<List<SportModel>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e("HOME_DEBUG", "Failed to load dynamic sports: " + t.getMessage());
                loadFallbackSports();
            }
        });
    }

    private void loadFallbackSports() {
        List<SportModel> fallbackSports = DummySportRepository.getInstance().getAllSports();
        adapter = new SportAdapter(fallbackSports);
        recyclerSports.setAdapter(adapter);
    }
}
