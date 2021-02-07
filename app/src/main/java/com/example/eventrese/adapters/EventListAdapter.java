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
import com.example.eventrese.ui.EventDetailActivity;
import com.example.eventrese.util.OnEventSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private List<Event> mEvents;
    private Context mContext;
    private OnEventSelectedListener meventSelectedListener;

    public EventListAdapter(Context context, List<Event> events, OnEventSelectedListener eventSelectedListener){
        mContext = context;
        mEvents = events;
        meventSelectedListener = eventSelectedListener;
    }

    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view, mEvents, meventSelectedListener);
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
        @BindView(R.id.eventImageView) ImageView meventImageView;
        @BindView(R.id.eventNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;

        private Context mContext;
        private int mOrientation;
        private List<Event> mEvents;
        private OnEventSelectedListener eventSelectedListener;

        public EventViewHolder(View itemView, List<Event> events, OnEventSelectedListener eventSelectedListener){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();

            mOrientation = itemView.getResources().getConfiguration().orientation;

            mEvents = events;

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(0);
            }
            itemView.setOnClickListener(this);
        }

        private void createDetailFragment(int position){

            EventDetailFragment detailFragment = EventDetailFragment.newInstance(mEvents, position, Constants.SOURCE_FIND);
            FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.eventDetailContainer, detailFragment);
            // Commits these changes:
            ft.commit();
        }

        public void bindEvents(Event event) {
            mNameTextView.setText(event.getName());
            mCategoryTextView.setText(event.getCategory());
            Picasso.get().load(event.getImageUrl()).into(meventImageView);
        }

        @Override
        public void onClick(View v){
            int itemPosition = getLayoutPosition();
            eventSelectedListener.onEventSelected(itemPosition, mEvents, Constants.SOURCE_FIND);
            if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(itemPosition);
            } else {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_EVENTS, Parcels.wrap(mEvents));
                intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_FIND);
                mContext.startActivity(intent);
            }
        }
    }
}