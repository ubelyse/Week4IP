package com.example.eventrese.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindEvents extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mSearchedLocationReference;

    private ValueEventListener mSearchedLocationReferenceListener;

    @BindView(R.id.findeventsbtn) Button findEventsbtn;
    @BindView(R.id.savedEvents) Button msavedEvents;
    @BindView(R.id.locationEditText) EditText mLocationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Locations updated", "location: " + location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_events);
        ButterKnife.bind(this);

        findEventsbtn.setOnClickListener(this);
        msavedEvents.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findEventsbtn) {
            String location = mLocationEditText.getText().toString();

            saveLocationToFirebase(location);

            Intent intent = new Intent(FindEvents.this, EventsActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }

        if (v == msavedEvents) {
            Intent intent = new Intent(FindEvents.this, SavedEventstListActivity.class);
            startActivity(intent);
        }
    }

    public void saveLocationToFirebase(String location) {
        mSearchedLocationReference.push().setValue(location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }

}