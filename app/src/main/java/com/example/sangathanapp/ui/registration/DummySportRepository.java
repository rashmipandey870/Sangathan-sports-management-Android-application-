package com.example.sangathanapp.ui.registration;

import java.util.ArrayList;
import java.util.List;
import com.example.sangathanapp.R;



public class DummySportRepository {

    private static DummySportRepository instance;
    private final List<SportModel> sportList;

    private DummySportRepository() {
        sportList = new ArrayList<>();
        loadSports();
    }

    public static DummySportRepository getInstance() {
        if (instance == null) {
            instance = new DummySportRepository();
        }
        return instance;
    }

    private void loadSports() {

        sportList.add(new SportModel(
                1,
                "Cricket",
                "Inter-department cricket tournament",
                "20 Jan 2025",
                "Main Ground",
                "Department Level",
                200,
                156,
                R.drawable.ic_cricket
        ));

        sportList.add(new SportModel(
                2,
                "Football",
                "Knockout football championship",
                "25 Jan 2025",
                "Football Ground",
                "Institute Level",
                150,
                98,
                R.drawable.ic_football
        ));

        sportList.add(new SportModel(
                3,
                "Badminton",
                "Singles & doubles matches",
                "28 Jan 2025",
                "Indoor Stadium",
                "Open Level",
                80,
                60,
                R.drawable.ic_badminton
        ));
        sportList.add(new SportModel(
                4,
                "Volleyball",
                "Singles & doubles matches",
                "28 Jan 2025",
                "Indoor Stadium",
                "Open Level",
                80,
                60,
                R.drawable.ic_volleyball
        ));
        sportList.add(new SportModel(
                5,
                "Kabaddi",
                "Singles & doubles matches",
                "28 Jan 2025",
                "Indoor Stadium",
                "Open Level",
                80,
                60,
                R.drawable.ic_kabaddi
        ));
        sportList.add(new SportModel(
                6,
                "Carramboard",
                "Singles & doubles matches",
                "28 Jan 2025",
                "Indoor Stadium",
                "Open Level",
                80,
                60,
                R.drawable.ic_carramboard
        ));
        sportList.add(new SportModel(
                7,
                "Basketball",
                "Singles & doubles matches",
                "28 Jan 2025",
                "Indoor Stadium",
                "Open Level",
                80,
                60,
                R.drawable.ic_basketball
        ));
    }

    public List<SportModel> getAllSports() {
        return sportList;
    }

    public SportModel getSportById(int id) {
        for (SportModel sport : sportList) {
            if (sport.getId() == id) return sport;
        }
        return null;
    }
}
