package com.example.sangathanapp.ui.coordinator;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangathanapp.ApiClient;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.network.*;
import com.example.sangathanapp.ui.matches.FixtureAdapter;
import com.example.sangathanapp.ui.matches.FixtureModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class MatchDetailsFragment extends Fragment {

    private RecyclerView recyclerFixtures;
    private Spinner genderSpinner;

    private Button btnAddDates, btnAddTimes, btnGenerate;

    private final List<String> dateList = new ArrayList<>();
    private final List<String> timeList = new ArrayList<>();

    private String selectedSport = "cricket";
    private String selectedGender = "BOYS";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_match_details, container, false);

        recyclerFixtures = view.findViewById(R.id.recycler_fixtures);
        recyclerFixtures.setLayoutManager(new LinearLayoutManager(getContext()));

        genderSpinner = view.findViewById(R.id.sp_gender);

        btnAddDates = view.findViewById(R.id.btn_add_dates);
        btnAddTimes = view.findViewById(R.id.btn_add_times);
        btnGenerate = view.findViewById(R.id.btn_generate_fixture);

        setupGenderSpinner();

        btnAddDates.setOnClickListener(v -> showDateDialog());
        btnAddTimes.setOnClickListener(v -> showTimeDialog());

        btnGenerate.setOnClickListener(v -> generateFixtures());

        loadFixtures();

        return view;
    }

    private void showDateDialog() {

        EditText input = new EditText(getContext());
        input.setHint("10-08-2026, 12-08-2026");

        new AlertDialog.Builder(getContext())
                .setTitle("Enter Dates")
                .setView(input)
                .setPositiveButton("Save", (d, w) -> {

                    String text = input.getText().toString().trim();

                    if (text.isEmpty()) {
                        Toast.makeText(getContext(), "Enter dates!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dateList.clear();

                    for (String s : text.split(",")) {
                        dateList.add(s.trim());
                    }

                    Log.d("DEBUG", "Dates Saved: " + dateList);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showTimeDialog() {

        EditText input = new EditText(getContext());
        input.setHint("10:00 AM, 2:00 PM");

        new AlertDialog.Builder(getContext())
                .setTitle("Enter Times")
                .setView(input)
                .setPositiveButton("Save", (d, w) -> {

                    String text = input.getText().toString().trim();

                    if (text.isEmpty()) {
                        Toast.makeText(getContext(), "Enter times!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    timeList.clear();

                    for (String s : text.split(",")) {
                        timeList.add(s.trim());
                    }

                    Log.d("DEBUG", "Times Saved: " + timeList);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void setupGenderSpinner() {

        String[] genders = {"BOYS", "GIRLS"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                genders
        );

        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedGender = genders[pos];
                loadFixtures(); // reload on change
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void generateFixtures() {
        Log.e("TEST"," BUTTON CLICKED");
        Log.d("DEBUG", "Sport: " + selectedSport);
        Log.d("DEBUG", "Gender: " + selectedGender);
        Log.d("DEBUG", "Dates: " + dateList);
        Log.d("DEBUG", "Times: " + timeList);

        if(dateList.isEmpty()||timeList.isEmpty())
        {
            Toast.makeText(getContext(), "Please add dates and times", Toast.LENGTH_SHORT).show();
            return;
        }

        GenerateFixtureRequest request =
                new GenerateFixtureRequest(
                        selectedSport,
                        selectedGender,
                        dateList,
                        timeList
                );

        FixtureApi api = ApiClient.getClient().create(FixtureApi.class);

        api.generateFixture(request).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call,
                                   Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Generated", Toast.LENGTH_SHORT).show();
                    loadFixtures();
                } else {
                    Toast.makeText(getContext(), "Error " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFixtures() {

        FixtureApi api = ApiClient.getClient().create(FixtureApi.class);

        api.getFixturesBySport(selectedSport, selectedGender)
                .enqueue(new Callback<List<FixtureModel>>() {

                    @Override

                    public void onResponse(Call<List<FixtureModel>> call,
                                           Response<List<FixtureModel>> response) {

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            List<FixtureModel> list = response.body();

                            Log.d("FIXTURE_DEBUG", "Fixtures: " + list.size());

                            if (list.isEmpty()) {
                                Toast.makeText(getContext(), "No fixtures found", Toast.LENGTH_SHORT).show();
                            }

                            FixtureAdapter adapter = new FixtureAdapter(list,true);
                            recyclerFixtures.setAdapter(adapter);
                            adapter.notifyDataSetChanged(); // FORCE REFRESH

                        } else {
                            Toast.makeText(getContext(), "Response failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<FixtureModel>> call, Throwable t) {
                        Toast.makeText(getContext(), "Load failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}