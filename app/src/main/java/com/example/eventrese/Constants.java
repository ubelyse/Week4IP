package com.example.eventrese;

public class Constants {
    public static final String YELP_TOKEN = BuildConfig.YELP_TOKEN;
    public static final String YELP_BASE_URL = "https://api.yelp.com/v3/events/";
    public static final String YELP_LOCATION_QUERY_PARAMETER = "location";


    public static final String PREFERENCES_LOCATION_KEY = "location";

    public static final String PREFERENCES_FRIENDS_KEY = "users";

    //Child node name for saving the values for searched Locations in Firebase
    public static final String FIREBASE_CHILD_SEARCHED_LOCATION = "searchedLocation";

    public static final String FIREBASE_CHILD_EVENTS= "events";
}
