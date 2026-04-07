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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

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