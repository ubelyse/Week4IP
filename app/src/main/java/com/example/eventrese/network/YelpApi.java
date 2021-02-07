package com.example.eventrese.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpApi {

    @GET("https://api.yelp.com/v3/events")
    Call<EventSearch> getEvents(
            @Query("location") String location,
            @Query("term") String term
    );
}