package com.example.sangathanapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.ui.StudentTeamFragment;
import com.example.sangathanapp.ui.events.EventsFragment;
import com.example.sangathanapp.ui.home.HomeFragment;
import com.example.sangathanapp.ui.profile.ProfileFragment;
import com.example.sangathanapp.ui.score.ScoresFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    // Fragments
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment eventsFragment = new EventsFragment();
    private final Fragment teamFragment = new StudentTeamFragment();
    private final Fragment scoresFragment = new ScoresFragment();
    private final Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(homeFragment);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment selected = null;

            if (item.getItemId() == R.id.nav_home) {
                selected = homeFragment;

            } else if (item.getItemId() == R.id.nav_events) {
                selected = eventsFragment;

            } else if (item.getItemId() == R.id.nav_team) {
                selected = teamFragment;

            } else if (item.getItemId() == R.id.nav_scores) {
                selected = scoresFragment;

            } else if (item.getItemId() == R.id.nav_profile) {
                selected = profileFragment;
            }

            if (selected != null) {
                loadFragment(selected);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}