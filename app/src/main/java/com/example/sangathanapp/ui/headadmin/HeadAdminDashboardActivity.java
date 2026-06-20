package com.example.sangathanapp.ui.headadmin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HeadAdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_admin_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav);

        loadFragment(new AdminOverviewFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment selected = null;

            if (item.getItemId() == R.id.nav_overview) {
                selected = new AdminOverviewFragment();
            } else if (item.getItemId() == R.id.nav_coordinators) {
                selected = new ManageCoordinatorsFragment();

            } else if (item.getItemId() == R.id.nav_analytics) {
                selected = new SystemAnalyticsFragment();
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