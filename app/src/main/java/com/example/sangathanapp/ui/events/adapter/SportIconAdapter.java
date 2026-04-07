package com.example.sangathanapp.ui.events.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.events.model.SportIconModel;

import java.util.List;

public class SportIconAdapter
        extends RecyclerView.Adapter<SportIconAdapter.ViewHolder> {

    public interface OnSportClickListener {
        void onSportSelected(String sport);
    }

    private final List<SportIconModel> sports;
    private final OnSportClickListener listener;
    private int selectedPosition = -1;

    public SportIconAdapter(List<SportIconModel> sports,
                            OnSportClickListener listener) {
        this.sports = sports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport_circle, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportIconModel sport = sports.get(position);

        holder.icon.setImageResource(sport.getIconRes());
        holder.name.setText(sport.getSport());

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_circle_dark_border);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_circle_dark);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            listener.onSportSelected(sport.getSport().toLowerCase());
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imgSportIcon);
            name = itemView.findViewById(R.id.tvSportName);
        }
    }
}