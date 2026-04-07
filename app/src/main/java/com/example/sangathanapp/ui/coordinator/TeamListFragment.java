package com.example.sangathanapp.ui.coordinator;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.network.TeamApi;
import com.example.sangathanapp.ui.registration.network.TeamModel;

import java.util.List;

import retrofit2.*;

public class TeamListFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_team_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_teams);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTeams(recyclerView);

        return view;
    }

    private void loadTeams(RecyclerView recyclerView) {

        TeamApi api = ApiClient.getClient().create(TeamApi.class);

        api.getTeams().enqueue(new Callback<List<TeamModel>>() {

            @Override
            public void onResponse(Call<List<TeamModel>> call,
                                   Response<List<TeamModel>> response) {

                if (response.isSuccessful() && response.body()!=null) {

                    recyclerView.setAdapter(
                            new TeamAdapter(response.body())
                    );
                }
            }

            @Override
            public void onFailure(Call<List<TeamModel>> call, Throwable t) {
                Toast.makeText(getContext(),"Failed to load teams",Toast.LENGTH_SHORT).show();
            }
        });
    }
}