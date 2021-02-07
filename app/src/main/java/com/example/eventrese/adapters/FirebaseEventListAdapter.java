package com.example.eventrese.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.fragments.EventDetailFragment;
import com.example.eventrese.models.Event;
import com.example.eventrese.ui.EventDetailActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseEventListAdapter extends FirebaseRecyclerAdapter<Event, FirebaseEventViewHolder> implements ItemTouchHelperAdapter {
    private Query mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Event> mRestaurants = new ArrayList<>();
    private int mOrientation;

    public FirebaseEventListAdapter(FirebaseRecyclerOptions<Event> options,
                                         Query ref,
                                         OnStartDragListener onStartDragListener,
                                         Context context){
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mRestaurants.add(dataSnapshot.getValue(Event.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseEventViewHolder firebaseEventViewHolder, int position, @NonNull Restaurant restaurant) {
        firebaseEventViewHolder.bindRestaurant(restaurant);

        mOrientation = firebaseEventViewHolder.itemView.getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE){
            createDetailFragment(0);
        }

        firebaseEventViewHolder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    mOnStartDragListener.onStartDrag(firebaseEventViewHolder);
                }
                return false;
            }
        });

        firebaseEventViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = firebaseEventViewHolder.getAdapterPosition();
                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                    createDetailFragment(itemPosition);
                } else {
                    Intent intent = new Intent(mContext, EventDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                    intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                    intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_SAVED);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    private void createDetailFragment(int position){
        // Creates new RestaurantDetailFragment with the given position:
        EventDetailFragment detailFragment = EventDetailFragment.newInstance(mRestaurants, position, Constants.SOURCE_SAVED);
        // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        //  Replaces the FrameLayout with the RestaurantDetailFragment:
        ft.replace(R.id.restaurantDetailContainer, detailFragment);
        // Commits these changes:
        ft.commit();
    }

    @NonNull
    @Override
    public FirebaseEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
        return new FirebaseEventViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        Collections.swap(mRestaurants, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position){
        mRestaurants.remove(position);
        getRef(position).removeValue();
    }

    private void setIndexInFirebase(){
        for(Event restaurant: mRestaurants){
            int index = mRestaurants.indexOf(restaurant);
            DatabaseReference ref = getRef(index);
            ref.child("index").setValue(Integer.toString(index));
        }
    }

    @Override
    public void stopListening(){
        super.stopListening();
        mRef.removeEventListener(mChildEventListener);
    }
}