package com.example.sangathanapp.ui.coordinator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.R;

public class CoordinatorProfileFragment extends Fragment {

    public CoordinatorProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_coordinator_profile,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = view.findViewById(R.id.tv_coordinator_name);
        TextView tvRole = view.findViewById(R.id.tv_coordinator_role);
        TextView tvEmail = view.findViewById(R.id.tv_coordinator_email);

        // TEMP data (later from backend / shared pref)
        tvName.setText("Coordinator");
        tvRole.setText("Sports Admin");
        tvEmail.setText("coordinator@sangathan.com");
    }
}