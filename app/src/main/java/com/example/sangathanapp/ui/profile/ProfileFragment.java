package com.example.sangathanapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.LoginActivity;
import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.settings.SettingsActivity;
import com.example.sangathanapp.ui.verification.EmailVerificationActivity;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone, tvBatch;
    private ImageButton btnSettings;
    private Button btnLogout, btnVerifyEmail;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        loadUserData();
        setupActions();

        return view;
    }

    private void initViews(View v) {
        tvName = v.findViewById(R.id.profile_name);
        tvEmail = v.findViewById(R.id.profile_email);
        tvPhone = v.findViewById(R.id.profile_phone);
        tvBatch = v.findViewById(R.id.profile_batch);

        btnSettings = v.findViewById(R.id.btn_settings);
        btnLogout = v.findViewById(R.id.btn_logout);
        btnVerifyEmail = v.findViewById(R.id.btn_verify_email);
    }

    private void loadUserData() {
        tvName.setText("Student");
        tvEmail.setText("student@amity.edu");
        tvPhone.setText("+91 XXXXXXXXXX");
        tvBatch.setText("2024 - 2028");
    }

    private void setupActions() {

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v ->
                    startActivity(new Intent(getActivity(), SettingsActivity.class))
            );
        }

        if (btnVerifyEmail != null) {
            btnVerifyEmail.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), EmailVerificationActivity.class);
                intent.putExtra("USER_EMAIL", tvEmail.getText().toString());
                startActivity(intent);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            });
        }
    }
}