package com.example.sangathanapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sangathanapp.R;
import com.example.sangathanapp.ui.registration.DummySportRepository;
import com.example.sangathanapp.ui.registration.SportModel;
import com.example.sangathanapp.ui.registration.SportRegistrationActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        setupSportClick(root, R.id.card_cricket, 1);
        setupSportClick(root, R.id.card_football, 2);
        setupSportClick(root, R.id.card_badminton, 3);
        setupSportClick(root, R.id.card_volleyball, 4);
        setupSportClick(root, R.id.card_kabaddi, 5);
        setupSportClick(root, R.id.card_carramboard, 6);
        setupSportClick(root, R.id.card_basketball, 7);

        return root;
    }


    private void setupSportClick(View root, int cardId, int sportId) {

        View card = root.findViewById(cardId);
        if (card == null) return;

        card.setOnClickListener(v -> {

            //  Get correct sport from repository
            SportModel sport =
                    DummySportRepository.getInstance().getSportById(sportId);

            if (sport == null) return;

            // Open registration page
            Intent intent = new Intent(
                    requireActivity(),
                    SportRegistrationActivity.class
            );


            intent.putExtra("SPORT_DATA", sport);

            startActivity(intent);
        });
    }
}
