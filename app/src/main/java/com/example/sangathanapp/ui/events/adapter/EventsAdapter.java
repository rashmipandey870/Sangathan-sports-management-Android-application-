package com.example.sangathanapp.ui.events.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.events.model.EventModel;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<EventModel> eventList;

    public EventsAdapter(List<EventModel> eventList) {
        this.eventList = eventList;
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
        EventModel event = eventList.get(position);


        holder.tvMatchTitle.setText(event.getMatchTitle());
        holder.tvMatchDateTime.setText(event.getDate() + " • " + event.getTime());
        holder.tvMatchVenue.setText("Venue: " + event.getVenue()); // Adding "Venue: " to match your XML design
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // ---------------- VIEW HOLDER ----------------
    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvMatchTitle, tvMatchDateTime, tvMatchVenue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvMatchTitle = itemView.findViewById(R.id.tv_match_title);
            tvMatchDateTime = itemView.findViewById(R.id.tv_match_datetime);
            tvMatchVenue = itemView.findViewById(R.id.tv_match_venue);
        }
    }
}
