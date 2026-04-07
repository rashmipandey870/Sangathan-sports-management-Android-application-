package com.example.sangathanapp.ui.coordinator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.captain.TeamManagementFragment;
import com.example.sangathanapp.ui.events.EventsFragment;
import com.example.sangathanapp.ui.events.adapter.EventsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoordinatorDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);

        loadFragment(new CreateTeamFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment selected = null;

            if (item.getItemId()== R.id.nav_create_team) {
                selected = new CreateTeamFragment();
            }
            else if (item.getItemId() == R.id.nav_admin_dashboard) {
                selected = new TeamListFragment();
            } else if (item.getItemId() == R.id.nav_admin_fixtures) {
                selected = new MatchDetailsFragment();
            } else if (item.getItemId() == R.id.nav_admin_trials) {
                selected = new EventsFragment();
            } else if (item.getItemId() == R.id.nav_admin_profile) {
                selected = new CoordinatorProfileFragment();
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
                .replace(R.id.admin_fragment_container, fragment)
                .commit();
    }
}