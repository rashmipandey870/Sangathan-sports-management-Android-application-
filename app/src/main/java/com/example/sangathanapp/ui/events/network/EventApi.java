package com.example.sangathanapp.ui.events.network;

import com.example.sangathanapp.ui.events.model.EventModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApi {

    @GET("fixtures/{sport}")
    Call<List<EventModel>> getUpcomingEvents(
            @Path("sport") String sport,
            @Query("gender") String gender
    );
}