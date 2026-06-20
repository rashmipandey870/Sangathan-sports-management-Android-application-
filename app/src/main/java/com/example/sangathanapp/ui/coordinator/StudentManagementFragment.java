package com.example.sangathanapp.ui.coordinator;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.StudentApiService;
import com.example.sangathanapp.ui.registration.StudentRegistrationModel;

import java.util.List;

import retrofit2.*;

public class StudentManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private Spinner spinnerTeam;

    private StudentApiService api;
    private StudentAdapter adapter;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_coordinator_student,
                container,
                false
        );

        recyclerView = v.findViewById(R.id.recycler_students);
        spinnerTeam = v.findViewById(R.id.spinner_team);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = ApiClient.getClient().create(StudentApiService.class);

        //  HIDE DROPDOWN
        spinnerTeam.setVisibility(View.GONE);

        //  GET TEAM NAME FROM LOGIN
        String teamName = requireContext()
                .getSharedPreferences("APP", 0)
                .getString("TEAM_NAME", "");

        // LOAD ONLY THIS TEAM
        loadTeamStudents(teamName);

        View btnLogout = v.findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(view -> {
                // Clear login preferences
                android.content.SharedPreferences sp = requireContext().getSharedPreferences("APP", android.content.Context.MODE_PRIVATE);
                sp.edit().clear().apply();

                // Navigate to RoleSelectionActivity
                android.content.Intent intent = new android.content.Intent(requireContext(), com.example.sangathanapp.ui.RoleSelectionActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        return v;
    }
    // ================= TEAM DROPDOWN =================
    private void setupTeamSpinner() {

        // TODO: later load dynamically from backend
        String[] teams = {
                "CSE_CRICKET_BOYS",
                "ECE_CRICKET_BOYS",
                "BBA_FOOTBALL_GIRLS"
        };

        spinnerTeam.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                teams
        ));

        spinnerTeam.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        String teamName =
                                spinnerTeam.getSelectedItem().toString();

                        loadTeamStudents(teamName);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );
    }

    // ================= LOAD TEAM STUDENTS =================
    private void loadTeamStudents(String teamName) {

        api.getTeamStudents(teamName)
                .enqueue(new Callback<List<StudentRegistrationModel>>() {

                    @Override
                    public void onResponse(
                            Call<List<StudentRegistrationModel>> call,
                            Response<List<StudentRegistrationModel>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            adapter = new StudentAdapter(response.body(), "CAPTAIN");
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(getContext(),
                                    "No students found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<StudentRegistrationModel>> call,
                            Throwable t
                    ) {
                        Toast.makeText(getContext(),
                                "Server error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}