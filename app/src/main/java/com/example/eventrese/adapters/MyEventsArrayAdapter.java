package com.example.eventrese.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MyEventsArrayAdapter extends ArrayAdapter {
    private Context mContext;
    private String[] mEvents;
    private String[] mCuisines;

    public MyEventsArrayAdapter(Context mContext, int resource, String[] mEvents, String[] mCuisines) {
        super(mContext, resource);
        this.mContext = mContext;
        this.mEvents = mEvents;
        this.mCuisines = mCuisines;
    }

    @Override
    public Object getItem(int position) {
        String events = mEvents[position];
        String kitchen = mCuisines[position];
        return String.format("%s \nServes great: %s", events, kitchen);
    }

    @Override
    public int getCount() {
        return mEvents.length;
    }
}
