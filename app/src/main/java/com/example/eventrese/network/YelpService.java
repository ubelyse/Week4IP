package com.example.eventrese.network;


import java.io.IOException;

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

    /*public static void findEvents(String location, Callback callback){
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

    public ArrayList<Event> processResults(Response response){
        ArrayList<Event> events = new ArrayList<>();
        try{
            String jsonData = response.body().string();
            JSONObject yelpJSON = new JSONObject(jsonData);
            JSONArray eventsJSON = yelpJSON.getJSONArray("events");
            if (response.isSuccessful()){
                for (int i = 0; i < eventsJSON.length(); i++){
                    JSONObject eventJSON = eventsJSON.getJSONObject(i);
                    Integer attendingCount=eventJSON.getInt("1");
                    String category=eventJSON.getString("category");
                    Double cost=eventJSON.getJSONObject("price").getDouble("cost");
                    Double costMax=eventJSON.getJSONObject("price").getDouble("costMax");
                    String description=eventJSON.getString("description");
                    String eventSiteUrl=eventJSON.getString("eventSiteUrl");
                    String id=eventJSON.getString("id");
                    String imageUrl=eventJSON.getString("imageUrl");
                    Integer interestedCount=eventJSON.getInt("interestedCount");
                    Boolean isCanceled=eventJSON.getBoolean("isCanceled");
                    Boolean isFree=eventJSON.getBoolean("isFree");
                    Boolean isOfficial=eventJSON.getBoolean("isOfficial");
                    Double latitude=eventJSON.getJSONObject("coordinates").getDouble("latitude");
                    Double longitude=eventJSON.getJSONObject("coordinates").getDouble("longitude");
                    String name=eventJSON.getString("name");
                    String ticketsUrl=eventJSON.getString("ticketsUrl");
                    String timeEnd=eventJSON.getString("timeEnd");
                    String timeStart=eventJSON.getString("timeStart");
                    String businessId=eventJSON.getString("businessId");
                    Location location = new Location();



                    Event event = new Event(attendingCount,category,cost,costMax,description,eventSiteUrl,id,imageUrl,interestedCount,
                            isCanceled,isFree,isOfficial,latitude,longitude,name,ticketsUrl,timeEnd,timeStart,location,businessId);
                    events.add(event);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }*/
}