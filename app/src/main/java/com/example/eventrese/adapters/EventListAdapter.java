package com.example.eventrese.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventrese.R;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;
import com.example.eventrese.ui.EventDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private List<Event> mEvents;
    private Context mContext;

    public EventListAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = events;
    }

    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventListAdapter.EventViewHolder holder, int position) {
        holder.bindEvents(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.eventImageView) ImageView mRestaurantImageView;
        @BindView(R.id.eventNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        //@BindView(R.id.ratingTextView) TextView mRatingTextView;

        private Context mContext;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindEvents(Event restaurant) {
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategory());
           // mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, EventDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("event", Parcels.wrap(mEvents));
            mContext.startActivity(intent);
        }
    }
}
