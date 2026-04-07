package com.example.sangathanapp.ui.matches;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.FixtureApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixtureAdapter
        extends RecyclerView.Adapter<FixtureAdapter.ViewHolder> {

    private final List<FixtureModel> fixtures;
    private final boolean isAdmin;

    public FixtureAdapter(List<FixtureModel> fixtures, boolean isAdmin) {
        this.fixtures = fixtures;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fixture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        FixtureModel f = fixtures.get(position);

        holder.tvMatchName.setText(
                f.getTeamA() + " vs " + f.getTeamB()
        );

        holder.tvMatchDate.setText(
                f.getMatchDate() != null
                        ? f.getMatchDate()
                        : "Date not assigned"
        );

        holder.tvMatchTime.setText(
                f.getMatchTime() != null
                        ? f.getMatchTime()
                        : "Time not assigned"
        );

        holder.tvMatchVenue.setText(
                f.getVenue() != null
                        ? f.getVenue()
                        : "Venue not assigned"
        );

        //  SHOW BUTTON ONLY FOR ADMIN
        if (isAdmin) {
            holder.btnAddVenue.setVisibility(View.VISIBLE);

            holder.btnAddVenue.setOnClickListener(v ->
                    showVenueDialog(v, f, holder)
            );

        } else {
            holder.btnAddVenue.setVisibility(View.GONE);
        }
    }

    private void showVenueDialog(View view,
                                 FixtureModel fixture,
                                 ViewHolder holder) {

        EditText input = new EditText(view.getContext());
        input.setHint("Enter venue");

        new AlertDialog.Builder(view.getContext())
                .setTitle("Update Venue")
                .setView(input)
                .setPositiveButton("Save", (d, w) -> {

                    String venue = input.getText().toString().trim();

                    if (!venue.isEmpty()) {
                        updateVenue(view, fixture.getId(), venue, holder);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateVenue(View view,
                             String fixtureId,
                             String venue,
                             ViewHolder holder) {

        FixtureApi api =
                ApiClient.getClient().create(FixtureApi.class);

        Map<String, String> body = new HashMap<>();
        body.put("venue", venue);

        api.updateVenue(fixtureId, body)
                .enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(Call<ApiResponse> call,
                                           Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            holder.tvMatchVenue.setText(venue);
                            Toast.makeText(view.getContext(),
                                    "Venue updated",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(view.getContext(),
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMatchName, tvMatchDate, tvMatchTime, tvMatchVenue;
        Button btnAddVenue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMatchName = itemView.findViewById(R.id.tv_match_name);
            tvMatchDate = itemView.findViewById(R.id.tv_match_date);
            tvMatchTime = itemView.findViewById(R.id.tv_match_time);
            tvMatchVenue = itemView.findViewById(R.id.tv_match_venue);
            btnAddVenue = itemView.findViewById(R.id.btn_add_venue);
        }
    }
}