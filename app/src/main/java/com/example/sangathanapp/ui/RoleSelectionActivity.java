package com.example.sangathanapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sangathanapp.LoginActivity;
import com.example.sangathanapp.R;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Restore theme from SharedPreferences using ThemeHelper
        ThemeHelper.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        android.content.SharedPreferences sp = getSharedPreferences("APP", MODE_PRIVATE);
        boolean isDarkMode = sp.getBoolean("DARK_MODE", true);

        com.google.android.material.switchmaterial.SwitchMaterial switchTheme = findViewById(R.id.switch_theme_role);
        if (switchTheme != null) {
            switchTheme.setChecked(isDarkMode);
            switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                android.content.SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("DARK_MODE", isChecked);
                editor.apply();
                if (isChecked) {
                    androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
                }
                recreate();
            });
        }

        Button btnStudent = findViewById(R.id.btn_student);
        Button btnCaptain = findViewById(R.id.btn_captain);
        Button btnCoordinator = findViewById(R.id.btn_coordinator);
        Button btnHeadAdmin = findViewById(R.id.btn_head_admin);

        btnStudent.setOnClickListener(v -> openLogin("student"));
        btnCaptain.setOnClickListener(v -> openLogin("captain"));
        btnCoordinator.setOnClickListener(v -> openLogin("coordinator"));
        btnHeadAdmin.setOnClickListener(v -> openLogin("headadmin"));
    }

    private void openLogin(String role) {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("USER_ROLE", role);
        startActivity(intent);
    }
}