package com.example.sangathanapp.ui.captain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.ApiResponse;
import com.example.sangathanapp.ui.coordinator.network.TrialApi;
import com.example.sangathanapp.ui.coordinator.network.TrialRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptainTrialFragment extends Fragment {

    EditText etDate, etTime, etVenue, etInstruction;
    Button btnCreate;
    String teamId;
    String teamName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_captain_trial, container, false);

        etDate = v.findViewById(R.id.et_date);
        etTime = v.findViewById(R.id.et_time);
        etVenue = v.findViewById(R.id.et_venue);
        etInstruction = v.findViewById(R.id.et_instruction);
        btnCreate = v.findViewById(R.id.btn_create_trial);



        //  GET TEAM ID
        teamId = requireContext()
                .getSharedPreferences("APP", 0)
                .getString("TEAM_ID", "NOT_FOUND");

        teamName=requireContext().getSharedPreferences("APP",0).getString("TEAM_NAME","");


        Log.d("TRIAL_DEBUG", "TEAM_ID FROM PREF: " + teamId);

        btnCreate.setOnClickListener(v1 -> createTrial());

        return v;
    }

    private void createTrial() {

        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String venue = etVenue.getText().toString().trim();
        String instructions = etInstruction.getText().toString().trim();

        Log.d("TRIAL_DEBUG", "TEAM ID: " + teamId);

        //  VALIDATION
        if (teamId.equals("NOT_FOUND") || teamId.isEmpty()) {
            Toast.makeText(getContext(), "Team ID missing! Login again", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        TrialApi api = ApiClient.getClient().create(TrialApi.class);

        TrialRequest request = new TrialRequest(
                teamId,
                teamName,
                date,
                time,
                venue,
                instructions
        );

        api.createTrial(request).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> res) {

                Log.d("TRIAL_DEBUG", "CODE: " + res.code());

                if (res.isSuccessful()) {

                    Toast.makeText(getContext(), "Trial Created", Toast.LENGTH_SHORT).show();

                    etDate.setText("");
                    etTime.setText("");
                    etVenue.setText("");
                    etInstruction.setText("");

                } else {

                    Toast.makeText(getContext(), "Failed ", Toast.LENGTH_SHORT).show();

                    try {
                        Log.d("TRIAL_DEBUG", "ERROR: " + res.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("TRIAL_DEBUG", "FAIL: " + t.getMessage());
                Toast.makeText(getContext(), "Server Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}