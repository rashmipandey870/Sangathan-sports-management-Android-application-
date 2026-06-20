package com.example.sangathanapp.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.events.adapter.EventsAdapter;
import com.example.sangathanapp.ui.events.adapter.SportIconAdapter;
import com.example.sangathanapp.ui.events.model.EventModel;
import com.example.sangathanapp.ui.events.model.SportIconModel;
import com.example.sangathanapp.ui.events.network.EventApi;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView, sportRecyclerView;
    private EventsAdapter eventsAdapter;
    private Spinner genderSpinner;
    private androidx.appcompat.widget.SwitchCompat myTeamSwitch;

    private final List<EventModel> eventList = new ArrayList<>();
    private final List<EventModel> allEvents = new ArrayList<>();
    private final List<String> registeredTeams = new ArrayList<>();

    private String selectedSport = "cricket";
    private String selectedGender = "BOYS";
    private boolean showMyTeamOnly = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // RecyclerView for matches
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventsAdapter = new EventsAdapter(eventList);
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Sport icons
        sportRecyclerView = view.findViewById(R.id.sportRecyclerView);
        sportRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        // Gender & Team Filter UI binding
        genderSpinner = view.findViewById(R.id.sp_events_gender);
        myTeamSwitch = view.findViewById(R.id.switch_my_team);

        setupGenderSpinner();
        setupTeamSwitch();

        // Load Student registrations to support filtering
        loadStudentRegistrations();

        // Dynamically load sports from backend
        loadSports();

        return view;
    }

    private void setupGenderSpinner() {
        String[] genders = {"BOYS", "GIRLS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                genders
        );
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genders[position];
                loadUpcomingMatches();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupTeamSwitch() {
        myTeamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showMyTeamOnly = isChecked;
            filterMatchesLocally();
        });
    }

    private void loadStudentRegistrations() {
        String username = requireContext()
                .getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
                .getString("STUDENT_USERNAME", "");

        if (username.isEmpty()) return;

        com.example.sangathanapp.ui.registration.network.StudentRegistrationApi api =
                ApiClient.getClient().create(com.example.sangathanapp.ui.registration.network.StudentRegistrationApi.class);

        api.getStudentStatus(username).enqueue(new Callback<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>>() {
            @Override
            public void onResponse(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call,
                                   Response<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registeredTeams.clear();
                    for (com.example.sangathanapp.ui.registration.StudentRegistrationModel r : response.body()) {
                        if (r.getTeamName() != null) {
                            registeredTeams.add(r.getTeamName().toUpperCase().trim());
                        }
                    }
                    Log.d("EVENTS_DEBUG", "Loaded registered teams: " + registeredTeams);
                }
            }

            @Override
            public void onFailure(Call<List<com.example.sangathanapp.ui.registration.StudentRegistrationModel>> call, Throwable t) {
                Log.e("EVENTS_DEBUG", "Failed to fetch student registrations: " + t.getMessage());
            }
        });
    }

    private void loadSports() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        SportApi api = ApiClient.getClient().create(SportApi.class);
        api.getSports(currentYear).enqueue(new Callback<List<SportModel>>() {
            @Override
            public void onResponse(Call<List<SportModel>> call, Response<List<SportModel>> response) {
                if (!isAdded()) return;

                List<SportIconModel> sportsList = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    for (SportModel s : response.body()) {
                        sportsList.add(new SportIconModel(s.getName().toLowerCase(), s.getIconResId()));
                    }
                } else {
                    // Fallback
                    sportsList.add(new SportIconModel("cricket", R.drawable.ic_cricket));
                    sportsList.add(new SportIconModel("football", R.drawable.ic_football));
                    sportsList.add(new SportIconModel("badminton", R.drawable.ic_badminton));
                    sportsList.add(new SportIconModel("basketball", R.drawable.ic_basketball));
                    sportsList.add(new SportIconModel("volleyball", R.drawable.ic_volleyball));
                    sportsList.add(new SportIconModel("carramboard", R.drawable.ic_carramboard));
                }

                SportIconAdapter adapter = new SportIconAdapter(sportsList, EventsFragment.this::onSportSelected);
                sportRecyclerView.setAdapter(adapter);

                if (!sportsList.isEmpty()) {
                    selectedSport = sportsList.get(0).getSport();
                    loadUpcomingMatches();
                }
            }

            @Override
            public void onFailure(Call<List<SportModel>> call, Throwable t) {
                if (!isAdded()) return;

                List<SportIconModel> sportsList = new ArrayList<>();
                sportsList.add(new SportIconModel("cricket", R.drawable.ic_cricket));
                sportsList.add(new SportIconModel("football", R.drawable.ic_football));
                sportsList.add(new SportIconModel("badminton", R.drawable.ic_badminton));
                sportsList.add(new SportIconModel("basketball", R.drawable.ic_basketball));
                sportsList.add(new SportIconModel("volleyball", R.drawable.ic_volleyball));
                sportsList.add(new SportIconModel("carramboard", R.drawable.ic_carramboard));

                SportIconAdapter adapter = new SportIconAdapter(sportsList, EventsFragment.this::onSportSelected);
                sportRecyclerView.setAdapter(adapter);

                if (!sportsList.isEmpty()) {
                    selectedSport = sportsList.get(0).getSport();
                    loadUpcomingMatches();
                }
            }
        });
    }

    private void onSportSelected(String sport) {
        selectedSport = sport.toLowerCase();
        loadUpcomingMatches();
    }

    private void loadUpcomingMatches() {
        EventApi api = ApiClient.getClient().create(EventApi.class);
        api.getUpcomingEvents(selectedSport, selectedGender).enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    allEvents.clear();
                    allEvents.addAll(response.body());
                    filterMatchesLocally();
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(
                        requireContext(),
                        "Failed to load matches",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void filterMatchesLocally() {
        List<EventModel> filtered = new ArrayList<>();
        for (EventModel event : allEvents) {
            // Filter: only show if winner is null or empty (scheduled/upcoming match)
            if (event.getWinner() != null && !event.getWinner().trim().isEmpty()) {
                continue;
            }
            if (showMyTeamOnly) {
                String teamA = event.getTeamA() != null ? event.getTeamA().toUpperCase().trim() : "";
                String teamB = event.getTeamB() != null ? event.getTeamB().toUpperCase().trim() : "";
                if (registeredTeams.contains(teamA) || registeredTeams.contains(teamB)) {
                    filtered.add(event);
                }
            } else {
                filtered.add(event);
            }
        }
        eventList.clear();
        eventList.addAll(filtered);
        eventsAdapter.notifyDataSetChanged();
    }
}