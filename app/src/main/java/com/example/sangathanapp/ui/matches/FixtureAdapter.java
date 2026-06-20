package com.example.sangathanapp.ui.matches;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

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

        holder.tvTeamAName.setText(f.getTeamA());
        holder.tvTeamBName.setText(f.getTeamB());

        holder.tvScoreA.setText(f.getScoreA() != null ? f.getScoreA() : "");
        holder.tvScoreB.setText(f.getScoreB() != null ? f.getScoreB() : "");

        updateWinnerUI(f, holder);

        if (isAdmin) {
            holder.cbTeamA.setVisibility(View.GONE);
            holder.cbTeamB.setVisibility(View.GONE);

            holder.layoutCoordinatorWinner.setVisibility(View.VISIBLE);

            final List<String> options = new ArrayList<>();
            options.add("Select Winner");
            options.add(f.getTeamA());
            options.add(f.getTeamB());

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                options
            );
            holder.spWinnerSelector.setAdapter(spinnerAdapter);

            if (f.getWinner() != null && !f.getWinner().isEmpty()) {
                int idx = options.indexOf(f.getWinner());
                if (idx != -1) {
                    holder.spWinnerSelector.setSelection(idx);
                } else {
                    holder.spWinnerSelector.setSelection(0);
                }
            } else {
                holder.spWinnerSelector.setSelection(0);
            }

            holder.btnConfirmWinner.setOnClickListener(v -> {
                int selectedPos = holder.spWinnerSelector.getSelectedItemPosition();
                if (selectedPos == 0) {
                    Toast.makeText(v.getContext(), "Please select a winner first", Toast.LENGTH_SHORT).show();
                    return;
                }
                String winner = options.get(selectedPos);
                updateWinner(holder.itemView, f, winner, holder);
            });

            holder.btnAddVenue.setVisibility(View.VISIBLE);
            holder.btnAddVenue.setOnClickListener(v ->
                    showVenueDialog(v, f, holder)
            );

        } else {
            holder.cbTeamA.setVisibility(View.GONE);
            holder.cbTeamB.setVisibility(View.GONE);
            holder.layoutCoordinatorWinner.setVisibility(View.GONE);
            holder.btnAddVenue.setVisibility(View.GONE);
        }
    }

    private void showVenueDialog(View view,
                                 FixtureModel fixture,
                                 ViewHolder holder) {

        View dialogView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.dialog_edit_fixture, null);

        EditText etDate = dialogView.findViewById(R.id.et_dialog_match_date);
        EditText etTime = dialogView.findViewById(R.id.et_dialog_match_time);
        EditText etVenue = dialogView.findViewById(R.id.et_dialog_match_venue);
        EditText etScoreA = dialogView.findViewById(R.id.et_dialog_score_a);
        EditText etScoreB = dialogView.findViewById(R.id.et_dialog_score_b);

        if (fixture.getMatchDate() != null) etDate.setText(fixture.getMatchDate());
        if (fixture.getMatchTime() != null) etTime.setText(fixture.getMatchTime());
        if (fixture.getVenue() != null) etVenue.setText(fixture.getVenue());
        if (fixture.getScoreA() != null) etScoreA.setText(fixture.getScoreA());
        if (fixture.getScoreB() != null) etScoreB.setText(fixture.getScoreB());

        new AlertDialog.Builder(view.getContext())
                .setView(dialogView)
                .setPositiveButton("Save", (d, w) -> {
                    String date = etDate.getText().toString().trim();
                    String time = etTime.getText().toString().trim();
                    String venue = etVenue.getText().toString().trim();
                    String scoreA = etScoreA.getText().toString().trim();
                    String scoreB = etScoreB.getText().toString().trim();

                    if (date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
                        Toast.makeText(view.getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updateFixtureDetails(view, fixture, date, time, venue, scoreA, scoreB, holder);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateFixtureDetails(View view,
                                     FixtureModel fixture,
                                     String date,
                                     String time,
                                     String venue,
                                     String scoreA,
                                     String scoreB,
                                     ViewHolder holder) {

        FixtureApi api = ApiClient.getClient().create(FixtureApi.class);

        Map<String, String> body = new HashMap<>();
        body.put("venue", venue);
        body.put("matchDate", date);
        body.put("matchTime", time);
        body.put("scoreA", scoreA);
        body.put("scoreB", scoreB);

        api.updateVenue(fixture.getId(), body)
                .enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(Call<ApiResponse> call,
                                           Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            fixture.setMatchDate(date);
                            fixture.setMatchTime(time);
                            fixture.setVenue(venue);
                            fixture.setScoreA(scoreA);
                            fixture.setScoreB(scoreB);

                            holder.tvMatchDate.setText(date);
                            holder.tvMatchTime.setText(time);
                            holder.tvMatchVenue.setText(venue);
                            holder.tvScoreA.setText(scoreA);
                            holder.tvScoreB.setText(scoreB);

                            Toast.makeText(view.getContext(),
                                    "Match details updated",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(),
                                    "Update failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(view.getContext(),
                                "Server error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateWinnerUI(FixtureModel f, ViewHolder holder) {
        boolean isWinnerA = f.getWinner() != null && f.getWinner().equalsIgnoreCase(f.getTeamA());
        boolean isWinnerB = f.getWinner() != null && f.getWinner().equalsIgnoreCase(f.getTeamB());

        if (f.getTeamB() != null && f.getTeamB().equalsIgnoreCase("BYE")) {
            isWinnerA = true;
        } else if (f.getTeamA() != null && f.getTeamA().equalsIgnoreCase("BYE")) {
            isWinnerB = true;
        }

        if (isWinnerA) {
            holder.tvWinnerBadgeA.setVisibility(View.VISIBLE);
            holder.tvTeamAName.setTextColor(0xFFFFD700); // Gold
        } else {
            holder.tvWinnerBadgeA.setVisibility(View.GONE);
            holder.tvTeamAName.setTextColor(0xFFFFFFFF); // White
        }

        if (isWinnerB) {
            holder.tvWinnerBadgeB.setVisibility(View.VISIBLE);
            holder.tvTeamBName.setTextColor(0xFFFFD700); // Gold
        } else {
            holder.tvWinnerBadgeB.setVisibility(View.GONE);
            holder.tvTeamBName.setTextColor(0xFFFFFFFF); // White
        }
    }

    private void updateWinner(View view,
                              FixtureModel fixture,
                              String winner,
                              ViewHolder holder) {

        FixtureApi api =
                ApiClient.getClient().create(FixtureApi.class);

        Map<String, String> body = new HashMap<>();
        body.put("winner", winner);

        api.updateWinner(fixture.getId(), body)
                .enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(Call<ApiResponse> call,
                                           Response<ApiResponse> response) {

                        if (response.isSuccessful()) {
                            fixture.setWinner(winner);
                            updateWinnerUI(fixture, holder);
                            Toast.makeText(view.getContext(),
                                    winner.isEmpty() ? "Winner cleared" : "Winner updated: " + winner,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(),
                                    "Failed to update winner",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(view.getContext(),
                                "Server error: " + t.getMessage(),
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
        TextView tvTeamAName, tvTeamBName;
        CheckBox cbTeamA, cbTeamB;
        TextView tvWinnerBadgeA, tvWinnerBadgeB;
        TextView tvScoreA, tvScoreB;
        View layoutCoordinatorWinner;
        Spinner spWinnerSelector;
        Button btnConfirmWinner;
        Button btnAddVenue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMatchName = itemView.findViewById(R.id.tv_match_name);
            tvMatchDate = itemView.findViewById(R.id.tv_match_date);
            tvMatchTime = itemView.findViewById(R.id.tv_match_time);
            tvMatchVenue = itemView.findViewById(R.id.tv_match_venue);
            tvTeamAName = itemView.findViewById(R.id.tv_teamA_name);
            tvTeamBName = itemView.findViewById(R.id.tv_teamB_name);
            cbTeamA = itemView.findViewById(R.id.cb_teamA);
            cbTeamB = itemView.findViewById(R.id.cb_teamB);
            tvWinnerBadgeA = itemView.findViewById(R.id.tv_winner_badge_A);
            tvWinnerBadgeB = itemView.findViewById(R.id.tv_winner_badge_B);
            tvScoreA = itemView.findViewById(R.id.tv_score_A);
            tvScoreB = itemView.findViewById(R.id.tv_score_B);
            layoutCoordinatorWinner = itemView.findViewById(R.id.layout_coordinator_winner);
            spWinnerSelector = itemView.findViewById(R.id.sp_winner_selector);
            btnConfirmWinner = itemView.findViewById(R.id.btn_confirm_winner);
            btnAddVenue = itemView.findViewById(R.id.btn_add_venue);
        }
    }
}