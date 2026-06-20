package com.example.sangathanapp.ui.coordinator;

import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.StudentApiService;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.*;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentRegistrationModel> list;
    private String role;

    public StudentAdapter(List<StudentRegistrationModel> list, String role) {
        this.list = list;
        this.role = role;
    }

    @Override
    public int getItemViewType(int position) {
        if (role.equals("CAPTAIN")) return 1;
        else if (role.equals("COORDINATOR")) return 2;
        else if (role.equals("CAPTAIN_TEAM")) return 4;
        else return 3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_student_row, parent, false);

        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_student_management, parent, false);

        } else if (viewType == 4) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_captain_team_member, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_student_view, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        StudentRegistrationModel s = list.get(position);

        //  SAFE TEXT SET (NO CRASH)
        if (h.name != null) h.name.setText(s.getStudentName());
        if (h.department != null) h.department.setText(s.getDepartment());
        if (h.team != null) h.team.setText(s.getTeamName());
        if (h.gender != null) h.gender.setText(s.getGender());
        if (h.status != null) h.status.setText("Status: " + s.getStatus());

        StudentApiService api =
                ApiClient.getClient().create(StudentApiService.class);

        // ================= CAPTAIN =================
        if (role.equals("CAPTAIN")) {

            if (h.btnSelect != null)
                h.btnSelect.setVisibility(View.VISIBLE);

            if (h.btnReject != null)
                h.btnReject.setVisibility(View.VISIBLE);

            if (h.btnSelect != null)
                h.btnSelect.setOnClickListener(v -> {
                    api.selectPlayer(s.getId()).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call,
                                               Response<ApiResponse> response) {

                            if (response.isSuccessful()) {
                                s.setStatus("SELECTED");
                                if (h.status != null)
                                    h.status.setText("Status: SELECTED");

                                Toast.makeText(v.getContext(),
                                        "Selected", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {}
                    });
                });

            if (h.btnReject != null)
                h.btnReject.setOnClickListener(v -> {
                    api.rejectPlayer(s.getId()).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call,
                                               Response<ApiResponse> response) {

                            if (response.isSuccessful()) {
                                s.setStatus("REJECTED");
                                if (h.status != null)
                                    h.status.setText("Status: REJECTED");

                                Toast.makeText(v.getContext(),
                                        "Rejected", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {}
                    });
                });
        }

        // ================= COORDINATOR =================
        else if (role.equals("COORDINATOR")) {

            if (h.btnEdit != null) {
                h.btnEdit.setVisibility(View.VISIBLE);

                h.btnEdit.setOnClickListener(v -> {
                    showEditDialog(v, s, position);
                });
            }
        }
        // ================= CAPTAIN_TEAM =================
        else if (role.equals("CAPTAIN_TEAM")) {
            if (h.btnEdit != null) {
                h.btnEdit.setVisibility(View.VISIBLE);
                h.btnEdit.setOnClickListener(v -> {
                    showEditDialog(v, s, position);
                });
            }
            if (h.tvInitials != null && s.getStudentName() != null && !s.getStudentName().isEmpty()) {
                String name = s.getStudentName().trim();
                String initials = "";
                String[] parts = name.split("\\s+");
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    initials += parts[0].substring(0, 1).toUpperCase();
                }
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    initials += parts[1].substring(0, 1).toUpperCase();
                }
                h.tvInitials.setText(initials);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ================= EDIT DIALOG =================
    private void showEditDialog(View v, StudentRegistrationModel s, int position) {

        Context context = v.getContext();

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_edit_student, null);

        EditText etName = dialogView.findViewById(R.id.et_name1);
        EditText etDept = dialogView.findViewById(R.id.et_department1);
        EditText etPhone = dialogView.findViewById(R.id.et_phone1);

        etName.setText(s.getStudentName());
        etDept.setText(s.getDepartment());
        etPhone.setText(s.getPhone());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Edit Student")
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(btn -> {

            String name = etName.getText().toString().trim();
            String dept = etDept.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || dept.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            s.setStudentName(name);
            s.setDepartment(dept);
            s.setPhone(phone);

            StudentApiService api =
                    ApiClient.getClient().create(StudentApiService.class);

            api.editStudent(s.getId(), s).enqueue(new Callback<ApiResponse>() {

                @Override
                public void onResponse(Call<ApiResponse> call,
                                       Response<ApiResponse> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(context,
                                "Updated Successfully",
                                Toast.LENGTH_SHORT).show();

                        notifyItemChanged(position);
                        dialog.dismiss();

                    } else {
                        Toast.makeText(context,
                                "Update failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(context,
                            "Server error",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // ================= VIEW HOLDER =================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, department, team, gender, status, tvInitials;
        Button btnSelect, btnReject, btnEdit;

        public ViewHolder(@NonNull View v) {
            super(v);

            name = v.findViewById(R.id.tv_student_name);
            department = v.findViewById(R.id.tv_student_department);
            team = v.findViewById(R.id.tv_student_team);
            gender = v.findViewById(R.id.tv_student_gender);
            status = v.findViewById(R.id.tv_student_status);

            btnSelect = v.findViewById(R.id.btn_select);
            btnReject = v.findViewById(R.id.btn_reject);
            btnEdit = v.findViewById(R.id.btn_edit);
            tvInitials = v.findViewById(R.id.tv_student_initials);
        }
    }
}





















































