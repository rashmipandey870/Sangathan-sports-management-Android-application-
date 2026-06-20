package com.example.sangathanapp.ui.captain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.coordinator.StudentManagementFragment;
import com.example.sangathanapp.ui.events.EventsFragment;
import com.example.sangathanapp.ui.matches.MatchesFragment;
import com.example.sangathanapp.ui.profile.ProfileFragment;
import com.example.sangathanapp.ui.score.ScoresFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CaptainDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_dashboard);

        BottomNavigationView bottomNav =
                findViewById(R.id.captain_bottom_navigation);

        loadFragment(new StudentManagementFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment selected = null;

            if (item.getItemId() == R.id.nav_captain_matches) {
                selected = new StudentManagementFragment();
            }
            else if (item.getItemId() == R.id.nav_captain_events) {
                selected = new EventsFragment();
            }
            else if (item.getItemId() == R.id.nav_captain_team) {
                selected = new TeamManagementFragment();
            }
            else if (item.getItemId() == R.id.nav_captain_score)
            {
                selected = new ScoresFragment();
            }
            else if (item.getItemId() == R.id.nav_captain_profile) {
                selected = new CaptainTrialFragment();
            }

            if (selected != null) {
                loadFragment(selected);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.captain_fragment_container1, fragment)
                .commit();
    }
}