package com.example.sangathanapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sangathanapp.LoginActivity;
import com.example.sangathanapp.R;

public class SettingsActivity extends AppCompatActivity {

    // -------- UI ELEMENTS --------
    private View btnFeedback, btnContact, btnPrivacy;
    private Button btnLogout;
    private ImageView btn_back;
    private Toolbar toolbar;
    private com.google.android.material.switchmaterial.SwitchMaterial switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.sangathanapp.ui.ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setupToolbar();
        setupClickListeners();
    }
    // ================= INIT =================
    private void initViews() {
        btn_back = findViewById(R.id.btn_back_4);
        toolbar = findViewById(R.id.toolbar_settings);
        btnFeedback = findViewById(R.id.btn_feedback);
        btnContact = findViewById(R.id.btn_contact_us);
        btnPrivacy = findViewById(R.id.btn_privacy_policy);
        btnLogout = findViewById(R.id.btn_logout);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
    }

    // ================= TOOLBAR =================
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // ================= CLICK LISTENERS =================
    private void setupClickListeners() {
        android.content.SharedPreferences sp = getSharedPreferences("APP", MODE_PRIVATE);
        boolean isDarkMode = sp.getBoolean("DARK_MODE", true);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sp.edit().putBoolean("DARK_MODE", isChecked).apply();
            if (isChecked) {
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        });

        btn_back.setOnClickListener(v -> finish());

        btnFeedback.setOnClickListener(v ->
                Toast.makeText(this, "Feedback clicked", Toast.LENGTH_SHORT).show()
        );

        btnContact.setOnClickListener(v ->
                Toast.makeText(this, "Contact Us clicked", Toast.LENGTH_SHORT).show()
        );

        btnPrivacy.setOnClickListener(v ->
                Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show()
        );


        btnLogout.setOnClickListener(v -> LogoutUser());
    }

    // ================= LOGOUT LOGIC =================
    private void LogoutUser() {

        // OPTIONAL: Clear saved login data later (SharedPreferences)
        // SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        // prefs.edit().clear().apply();

        Intent intent = new Intent(SettingsActivity.this, com.example.sangathanapp.ui.RoleSelectionActivity.class);

        //  Clears all previous screens
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}
