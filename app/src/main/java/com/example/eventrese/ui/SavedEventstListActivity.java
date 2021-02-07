package com.example.eventrese.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.adapters.FirebaseEventViewHolder;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedEventstListActivity extends AppCompatActivity {

    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter<Events, FirebaseEventViewHolder> mFirebaseAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_EVENTS).child(uid);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter(){
        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(mRestaurantReference, Events.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Events, FirebaseEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseEventViewHolder firebaseEventViewHolder, int position, @NonNull Events restaurant) {
                firebaseEventViewHolder.bindRestaurant(restaurant);
            }

            @NonNull
            @Override
            public FirebaseEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
                return new FirebaseEventViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }
}
