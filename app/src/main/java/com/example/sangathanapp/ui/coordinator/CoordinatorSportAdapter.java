package com.example.sangathanapp.ui.coordinator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.SportModel;

import java.util.List;

public class CoordinatorSportAdapter
        extends RecyclerView.Adapter<CoordinatorSportAdapter.ViewHolder> {

    //  Click callback to Fragment
    public interface OnSportClick {
        void onClick(int sportId);
    }

    private final List<SportModel> sports;
    private final OnSportClick listener;

    //  NEW: Track selected item
    private int selectedPosition = RecyclerView.NO_POSITION;

    public CoordinatorSportAdapter(
            List<SportModel> sports,
            OnSportClick listener
    ) {
        this.sports = sports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_coordinator_sport_card,
                        parent,
                        false
                );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        SportModel sport = sports.get(position);

        holder.icon.setImageResource(sport.getIconResId());
        holder.name.setText(sport.getName());

        // VISUAL SELECTION STATE
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(
                    R.drawable.bg_sport_card_selected
            );
            holder.name.setTextColor(
                    holder.itemView.getResources()
                            .getColor(android.R.color.white)
            );
        } else {
            holder.itemView.setBackgroundResource(
                    R.drawable.bg_sport_card_unselected
            );
            holder.name.setTextColor(
                    holder.itemView.getResources()
                            .getColor(android.R.color.black)
            );
        }

        holder.itemView.setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);

            listener.onClick(sport.getId());
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.img_sport_icon);
            name = itemView.findViewById(R.id.tv_sport_name);
        }
    }
}
