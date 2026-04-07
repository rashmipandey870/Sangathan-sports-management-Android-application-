package com.example.sangathanapp.ui.coordinator.network;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.network.TeamModel;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private List<TeamModel> teams;

    public TeamAdapter(List<TeamModel> teams) {
        this.teams = teams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TeamModel team = teams.get(position);

        holder.tvTeamName.setText(team.getTeamName());
        holder.tvCaptain.setText("Captain: " + team.getCaptainName());
        holder.tvDetails.setText(team.getDepartment() + " | " + team.getGender());

        holder.btnView.setOnClickListener(v -> {

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Opening " + team.getTeamName(),
                    Toast.LENGTH_SHORT
            ).show();

            // Later we open next screen
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    // VIEW HOLDER
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTeamName, tvCaptain, tvDetails;
        Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvCaptain = itemView.findViewById(R.id.tv_captain);
            tvDetails = itemView.findViewById(R.id.tv_details);
            btnView = itemView.findViewById(R.id.btn_view_team);
        }
    }
}