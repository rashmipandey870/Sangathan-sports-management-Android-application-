package com.example.sangathanapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.SportRegistrationActivity;

import java.util.ArrayList;
import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

    private final List<SportModel> sportsOriginal;
    private final List<SportModel> sportsFiltered;

    public SportAdapter(List<SportModel> sports) {
        this.sportsOriginal = sports;
        this.sportsFiltered = new ArrayList<>(sports);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportModel sport = sportsFiltered.get(position);

        holder.tvName.setText(sport.getName());
        holder.tvRegistered.setText("Registered: " + sport.getRegisteredPlayers() + "/" + sport.getMaxPlayers());
        holder.progressBar.setMax(sport.getMaxPlayers());
        holder.progressBar.setProgress(sport.getRegisteredPlayers());

        String sportName = sport.getName();
        if (sportName != null && !sportName.isEmpty()) {
            holder.tvInitial.setText(sportName.substring(0, 1).toUpperCase());
            int color;
            String nameLower = sportName.toLowerCase();
            if (nameLower.contains("cricket")) {
                color = 0xFFF97316; // Orange
            } else if (nameLower.contains("football")) {
                color = 0xFF22C55E; // Green
            } else if (nameLower.contains("basketball")) {
                color = 0xFFEAB308; // Amber
            } else if (nameLower.contains("volleyball")) {
                color = 0xFF06B6D4; // Cyan
            } else if (nameLower.contains("badminton")) {
                color = 0xFF8B5CF6; // Purple
            } else if (nameLower.contains("kabaddi")) {
                color = 0xFFEF4444; // Red
            } else {
                color = 0xFF64748B; // Slate
            }
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            drawable.setColor(color);
            holder.tvInitial.setBackground(drawable);
            holder.tvInitial.setVisibility(View.VISIBLE);
            holder.imgIcon.setVisibility(View.GONE);
        } else {
            holder.tvInitial.setVisibility(View.GONE);
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.imgIcon.setImageResource(sport.getIconResId());
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, SportRegistrationActivity.class);
            intent.putExtra("SPORT_DATA", sport);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sportsFiltered.size();
    }

    public void filter(String query) {
        sportsFiltered.clear();
        if (query == null || query.isEmpty()) {
            sportsFiltered.addAll(sportsOriginal);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (SportModel s : sportsOriginal) {
                if (s.getName() != null && s.getName().toLowerCase().contains(lowerCaseQuery)) {
                    sportsFiltered.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvInitial;
        TextView tvName;
        TextView tvRegistered;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_sport_icon);
            tvInitial = itemView.findViewById(R.id.tv_sport_initial);
            tvName = itemView.findViewById(R.id.tv_sport_name);
            tvRegistered = itemView.findViewById(R.id.tv_sport_registered);
            progressBar = itemView.findViewById(R.id.progress_sport_registered);
        }
    }
}
