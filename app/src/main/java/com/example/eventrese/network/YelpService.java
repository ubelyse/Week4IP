package com.example.eventrese.network;


import com.example.eventrese.Constants;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;
import com.example.eventrese.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.eventrese.Constants.YELP_BASE_URL;
import static com.example.eventrese.Constants.YELP_TOKEN;


public class YelpService {

    private static Retrofit retrofit = null;

    public static YelpApi getClient() {

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", YELP_TOKEN)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(YELP_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(YelpApi.class);
    }


    /*public static void findRestaurants(String location, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Constants.YELP_TOKEN)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Events> processResults(Response response){
        ArrayList<Events> restaurants = new ArrayList<>();
        try{
            String jsonData = response.body().string();
            JSONObject yelpJSON = new JSONObject(jsonData);
            JSONArray businessesJSON = yelpJSON.getJSONArray("events");
            if (response.isSuccessful()){
                for (int i = 0; i < businessesJSON.length(); i++){
                    JSONObject restaurantJSON = businessesJSON.getJSONObject(i);
                    Integer attendingCount= restaurantJSON.getInt("attendingCount");
                    String category = restaurantJSON.getString("category");
                    double cost=restaurantJSON.getJSONObject("cost").getDouble("cost");
                    double costMax=restaurantJSON.getJSONObject("costMax").getDouble("costMax");
                    String description=restaurantJSON.getString("description");
                    String id=restaurantJSON.getString("id");
                    String businessId=restaurantJSON.getString("businessId");

                    String event_site_url=restaurantJSON.getString("eventSiteUrl");
                    String imageUrl = restaurantJSON.getString("image_url");
                    Integer interestedCount= restaurantJSON.getInt("interestedCount");
                    double latitude = restaurantJSON.getJSONObject("coordinates").getDouble("latitude");
                    double longitude = restaurantJSON.getJSONObject("coordinates").getDouble("longitude");
                    String name = restaurantJSON.getString("name");
                    String ticketsUrl = restaurantJSON.getString("ticketsUrl");
                    String timeEnd=restaurantJSON.getString("timeEnd");
                    String timeStart=restaurantJSON.getString("timeStart");
                    Location location=new Location();

                    Boolean isCanceled=restaurantJSON.getBoolean("isCanceled");
                    Boolean isFree=restaurantJSON.getBoolean("isFree");
                    Boolean isOfficial=restaurantJSON.getBoolean("isOfficial");


                    ArrayList<String> address = new ArrayList<>();
                    JSONArray addressJSON = restaurantJSON.getJSONObject("location").getJSONArray("display_address");
                    for (int y = 0; y < addressJSON.length(); y++){
                        address.add(addressJSON.get(y).toString());
                    }
                    Events restaurant = new Events(attendingCount,category,cost,costMax,description,event_site_url,id,imageUrl,interestedCount,isCanceled,isFree,isOfficial,latitude,longitude,name,ticketsUrl, timeEnd,timeStart,businessId);
                    restaurants.add(restaurant);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurants;
    }*/
}