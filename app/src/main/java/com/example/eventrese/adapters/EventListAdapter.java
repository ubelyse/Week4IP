package com.example.eventrese.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.fragments.EventDetailFragment;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;
import com.example.eventrese.ui.EventDetailActivity;
import com.example.eventrese.util.OnEventSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private ArrayList<Event> mEvents = new ArrayList<>();
    private Context mContext;
    private OnEventSelectedListener eventSelectedListener;

    public EventListAdapter(Context context, ArrayList<Event> events, OnEventSelectedListener eventSelectedListener){
        mContext = context;
        mEvents = events;
        eventSelectedListener = eventSelectedListener;
    }

    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view, mEvents, eventSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventListAdapter.EventViewHolder holder, int position){
        holder.bindEvents(mEvents.get(position));
    }

    @Override
    public int getItemCount(){
        return mEvents.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

        private Context mContext;
        private int mOrientation;
        private ArrayList<Event> mEvents = new ArrayList<>();
        private OnEventSelectedListener eventSelectedListener;

        public EventViewHolder(View itemView, ArrayList<Event> events, OnEventSelectedListener eventSelectedListener){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            // Determines the current orientation of the device:
            mOrientation = itemView.getResources().getConfiguration().orientation;

            mEvents = events;
            eventSelectedListener = eventSelectedListener;

            // Checks if the recorded orientation matches Android's landscape configuration.
            // if so, we create a new DetailFragment to display in our special landscape layout:
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(0);
            }
            itemView.setOnClickListener(this);
        }

        // Takes position of restaurant in list as parameter:
        private void createDetailFragment(int position){
            // Creates new RestaurantDetailFragment with the given position:
            EventDetailFragment detailFragment = EventDetailFragment.newInstance(mEvents, position, Constants.SOURCE_FIND);
            // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
            FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
            //  Replaces the FrameLayout with the RestaurantDetailFragment:
            ft.replace(R.id.restaurantDetailContainer, detailFragment);
            // Commits these changes:
            ft.commit();
        }

        public void bindEvents(Event event) {
            mNameTextView.setText(event.getName());
            mCategoryTextView.setText(event.getCategory());
            Picasso.get().load(event.getImageUrl()).into(mRestaurantImageView);
        }

        @Override
        public void onClick(View v){
            // Determines the position of the restaurant clicked:
            int itemPosition = getLayoutPosition();
            eventSelectedListener.onRestaurantSelected(itemPosition, mEvents, Constants.SOURCE_FIND);
            if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(itemPosition);
            } else {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mEvents));
                intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_FIND);
                mContext.startActivity(intent);
            }
        }
    }
}