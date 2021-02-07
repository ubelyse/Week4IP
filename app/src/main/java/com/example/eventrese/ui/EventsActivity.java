package com.example.eventrese.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.ProgressBar;

import android.widget.TextView;
import com.example.eventrese.R;
import com.example.eventrese.adapters.EventListAdapter;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.EventSearch;
import com.example.eventrese.network.YelpApi;
import com.example.eventrese.network.YelpService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = EventsActivity.class.getSimpleName();

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private EventListAdapter mAdapter;

    public List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        YelpApi client = YelpService.getClient();

        Call<EventSearch> call = client.getEvents(location, "event");

        call.enqueue(new Callback<EventSearch>() {
            @Override
            public void onResponse(Call<EventSearch> call, Response<EventSearch> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    events = response.body().getEvents();
                    mAdapter = new EventListAdapter(EventsActivity.this, events);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(EventsActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    showRestaurants();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<EventSearch> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }

        });
    }
    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}