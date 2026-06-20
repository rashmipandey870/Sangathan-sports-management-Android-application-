package com.example.sangathanapp.ui.headadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
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
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.network.SportApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageSportsAdapter extends RecyclerView.Adapter<ManageSportsAdapter.ViewHolder> {

    private final List<SportModel> sports;
    private final Runnable onDataChanged;

    public ManageSportsAdapter(List<SportModel> sports, Runnable onDataChanged) {
        this.sports = sports;
        this.onDataChanged = onDataChanged;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_sport, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportModel sport = sports.get(position);

        holder.tvSportName.setText(sport.getName());
        holder.tvSportYear.setText("Year: " + sport.getYear());
        holder.tvDetails.setText("Level: " + sport.getLevel() + " | Venue: " + sport.getVenue());

        if (!TextUtils.isEmpty(sport.getCoordinatorName())) {
            holder.tvCoordName.setText("Name: " + sport.getCoordinatorName());
            holder.tvCoordEmail.setText("Email: " + sport.getCoordinatorEmail());
            holder.tvCoordPhone.setText("Phone: " + sport.getCoordinatorPhone());
        } else {
            holder.tvCoordName.setText("Name: Not Assigned");
            holder.tvCoordEmail.setText("Email: --");
            holder.tvCoordPhone.setText("Phone: --");
        }

        holder.btnRemove.setOnClickListener(v -> removeSport(v.getContext(), sport.getMongoId(), position));

        holder.btnAssign.setOnClickListener(v -> showAssignDialog(v.getContext(), sport));
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    private void removeSport(Context context, String mongoId, int position) {
        if (TextUtils.isEmpty(mongoId)) {
            Toast.makeText(context, "Cannot delete local sport placeholder", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("Delete Sport")
                .setMessage("Are you sure you want to remove this sport?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    SportApi api = ApiClient.getClient().create(SportApi.class);
                    api.removeSport(mongoId).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Sport removed", Toast.LENGTH_SHORT).show();
                                sports.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, sports.size());
                                if (onDataChanged != null) onDataChanged.run();
                            } else {
                                Toast.makeText(context, "Failed to remove sport", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(context, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAssignDialog(Context context, SportModel sport) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_assign_coordinator, null);
        EditText etName = view.findViewById(R.id.et_dialog_coord_name);
        EditText etEmail = view.findViewById(R.id.et_dialog_coord_email);
        EditText etPhone = view.findViewById(R.id.et_dialog_coord_phone);

        if (!TextUtils.isEmpty(sport.getCoordinatorName())) {
            etName.setText(sport.getCoordinatorName());
            etEmail.setText(sport.getCoordinatorEmail());
            etPhone.setText(sport.getCoordinatorPhone());
        }

        new AlertDialog.Builder(context)
                .setTitle("Assign Coordinator")
                .setView(view)
                .setPositiveButton("Assign", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();

                    if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, String> body = new HashMap<>();
                    body.put("coordinatorName", name);
                    body.put("coordinatorEmail", email);
                    body.put("coordinatorPhone", phone);

                    SportApi api = ApiClient.getClient().create(SportApi.class);
                    api.assignCoordinator(sport.getMongoId(), body).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Coordinator assigned", Toast.LENGTH_SHORT).show();
                                sport.setCoordinatorName(name);
                                sport.setCoordinatorEmail(email);
                                sport.setCoordinatorPhone(phone);
                                notifyDataSetChanged();
                                if (onDataChanged != null) onDataChanged.run();
                            } else {
                                Toast.makeText(context, "Assignment failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(context, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSportName, tvSportYear, tvDetails;
        TextView tvCoordName, tvCoordEmail, tvCoordPhone;
        Button btnAssign, btnRemove;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvSportName = v.findViewById(R.id.tv_admin_sport_name);
            tvSportYear = v.findViewById(R.id.tv_admin_sport_year);
            tvDetails = v.findViewById(R.id.tv_admin_sport_details);
            tvCoordName = v.findViewById(R.id.tv_admin_coord_name);
            tvCoordEmail = v.findViewById(R.id.tv_admin_coord_email);
            tvCoordPhone = v.findViewById(R.id.tv_admin_coord_phone);
            btnAssign = v.findViewById(R.id.btn_assign_coordinator);
            btnRemove = v.findViewById(R.id.btn_remove_sport);
        }
    }
}
