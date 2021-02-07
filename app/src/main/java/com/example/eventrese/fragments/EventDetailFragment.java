package com.example.eventrese.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.eventImageView) ImageView mImageLabel;
    @BindView(R.id.eventNameTextView) TextView mNameLabel;
    @BindView(R.id.categoryTextView) TextView mCategoriesLabel;
    @BindView(R.id.attendingTextView) TextView mRatingLabel;
    @BindView(R.id.websiteTextView) TextView mWebsiteLabel;
    @BindView(R.id.phoneTextView) TextView mPhoneLabel;
    @BindView(R.id.addressTextView) TextView mAddressLabel;
    @BindView(R.id.saveEventButton) TextView mSaveRestaurantButton;

    private Event mEvents;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment newInstance(Event event){
        EventDetailFragment restaurantDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", Parcels.wrap(event));
        restaurantDetailFragment.setArguments(args);
        return restaurantDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEvents = Parcels.unwrap(getArguments().getParcelable("event"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);
        Picasso.get()
                .load(mEvents.getImageUrl())
                .into(mImageLabel);
        mNameLabel.setText(mEvents.getName());
        mCategoriesLabel.setText(mEvents.getCategory());
       // mRatingLabel.setText(Double.toString(mRestaurant);
        //mPhoneLabel.setText(mRestaurant.getPhone());
       // mAddressLabel.setText(""+mRestaurant.getLocation());
        mWebsiteLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);
        mAddressLabel.setOnClickListener(this);
        mSaveRestaurantButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
        if (v == mWebsiteLabel){
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mEvents.getEventSiteUrl()));
            startActivity(webIntent);
        }
        /*if (v == mPhoneLabel) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mRestaurant.getPhone()));
            startActivity(phoneIntent);
        }*/
        if (v == mAddressLabel) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:" + mEvents.getLatitude()
                            + "," + mEvents.getLongitude()
                            + "?q=(" + mEvents.getName() + ")"));
            startActivity(mapIntent);
        } if (v == mSaveRestaurantButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference restaurantRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_EVENTS)
                    .child(uid);
            DatabaseReference pushRef = restaurantRef.push();
            String pushId = pushRef.getKey();
            mEvents.setId(pushId);
            pushRef.setValue(mEvents);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }

}