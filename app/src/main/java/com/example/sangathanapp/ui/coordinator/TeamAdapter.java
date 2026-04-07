package com.example.sangathanapp.ui.coordinator;

import android.content.*;
import android.view.*;
import android.widget.TextView;

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
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        TeamModel team = teams.get(position);

        holder.tvTeamName.setText(team.getTeamName());

        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent i = new Intent(context, TeamSelectionActivity.class);

            i.putExtra("TEAM_NAME", team.getTeamName());
            i.putExtra("ROLE", "COORDINATOR");

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTeamName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
        }
    }
}