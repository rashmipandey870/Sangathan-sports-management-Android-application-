package com.example.sangathanapp.ui.matches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.FixtureApi;
import com.example.sangathanapp.ui.matches.FixtureAdapter;
import com.example.sangathanapp.ui.matches.FixtureModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchesFragment extends Fragment {

    private RecyclerView recyclerFixtures;

    private String selectedSport = "cricket";
    private String selectedGender = "BOYS";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_matches,
                container,
                false
        );

        recyclerFixtures = view.findViewById(R.id.recycler_fixtures);

        recyclerFixtures.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        loadFixtures();

        return view;
    }

    private void loadFixtures() {

        FixtureApi api =
                ApiClient.getClient().create(FixtureApi.class);

        api.getFixturesBySport(selectedSport, selectedGender)
                .enqueue(new Callback<List<FixtureModel>>() {

                    @Override
                    public void onResponse(Call<List<FixtureModel>> call,
                                           Response<List<FixtureModel>> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful()
                                && response.body() != null) {

                            recyclerFixtures.setAdapter(
                                    new FixtureAdapter(response.body(), false)
                            );

                        } else {
                            Toast.makeText(getContext(),
                                    "No matches available",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FixtureModel>> call,
                                          Throwable t) {

                        Toast.makeText(getContext(),
                                "Failed to load matches",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}