package com.example.sangathanapp.ui.headadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.headadmin.network.LeaderboardEntry;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardEntry> list;

    public LeaderboardAdapter(List<LeaderboardEntry> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardEntry entry = list.get(position);
        int rank = position + 1;

        holder.tvRank.setText(String.valueOf(rank));
        holder.tvDeptName.setText(entry.getDepartment());
        holder.tvWins.setText(entry.getWins() + " Wins");

        // Premium ranking colors
        if (rank == 1) {
            holder.tvRank.setTextColor(0xFFFFD700); // Gold
            holder.tvWins.setTextColor(0xFFFFD700);
            holder.tvRank.setTextSize(16f);
        } else if (rank == 2) {
            holder.tvRank.setTextColor(0xFFC0C0C0); // Silver
            holder.tvWins.setTextColor(0xFFC0C0C0);
        } else if (rank == 3) {
            holder.tvRank.setTextColor(0xFFCD7F32); // Bronze
            holder.tvWins.setTextColor(0xFFCD7F32);
        } else {
            holder.tvRank.setTextColor(0xFF8A9CB4); // Slate
            holder.tvWins.setTextColor(0xFF1E88E5); // Blue
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvDeptName, tvWins;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvDeptName = itemView.findViewById(R.id.tv_dept_name);
            tvWins = itemView.findViewById(R.id.tv_wins);
        }
    }
}
