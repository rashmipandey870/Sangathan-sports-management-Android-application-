package com.example.sangathanapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.matches.FixtureModel;

import java.util.List;

public class UpcomingMatchesAdapter extends RecyclerView.Adapter<UpcomingMatchesAdapter.ViewHolder> {

    private final List<FixtureModel> matchList;

    public UpcomingMatchesAdapter(List<FixtureModel> matchList) {
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_match_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FixtureModel match = matchList.get(position);

        holder.tvTitle.setText(match.getTeamA() + " vs " + match.getTeamB());

        String dateStr = match.getMatchDate() != null && !match.getMatchDate().isEmpty() ? match.getMatchDate() : "Date TBD";
        String timeStr = match.getMatchTime() != null && !match.getMatchTime().isEmpty() ? match.getMatchTime() : "Time TBD";
        holder.tvDateTime.setText(dateStr + "  •  " + timeStr);

        String venueStr = match.getVenue() != null && !match.getVenue().isEmpty() ? match.getVenue() : "Venue TBD";
        holder.tvVenue.setText(venueStr);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDateTime, tvVenue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_match_title);
            tvDateTime = itemView.findViewById(R.id.tv_match_datetime);
            tvVenue = itemView.findViewById(R.id.tv_match_venue);
        }
    }
}
