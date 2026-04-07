package com.example.sangathanapp.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView eventsRecyclerView, sportRecyclerView;
    private EventsAdapter eventsAdapter;

    private final List<EventModel> eventList = new ArrayList<>();
    private final List<EventModel> allEvents = new ArrayList<>();

    private String selectedSport = "cricket";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // RecyclerView
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

        List<SportIconModel> sports = new ArrayList<>();
        sports.add(new SportIconModel("cricket", R.drawable.ic_cricket));
        sports.add(new SportIconModel("football", R.drawable.ic_football));
        sports.add(new SportIconModel("badminton", R.drawable.ic_badminton));
        sports.add(new SportIconModel("basketball", R.drawable.ic_basketball));
        sports.add(new SportIconModel("volleyball", R.drawable.ic_volleyball));
        sports.add(new SportIconModel("carramboard", R.drawable.ic_carramboard));

        SportIconAdapter adapter =
                new SportIconAdapter(sports, this::onSportSelected);

        sportRecyclerView.setAdapter(adapter);

        // Initial load
        loadUpcomingMatches(selectedSport);

        return view;
    }


    private void onSportSelected(String sport) {
        selectedSport = sport.toLowerCase();
        loadUpcomingMatches(selectedSport);
    }


    private void loadUpcomingMatches(String sport) {

        EventApi api = ApiClient.getClient().create(EventApi.class);

        api.getUpcomingEvents(sport).enqueue(new Callback<List<EventModel>>() {

            @Override
            public void onResponse(
                    Call<List<EventModel>> call,
                    Response<List<EventModel>> response) {

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    eventList.clear();
                    eventList.addAll(response.body());
                    eventsAdapter.notifyDataSetChanged();
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
}