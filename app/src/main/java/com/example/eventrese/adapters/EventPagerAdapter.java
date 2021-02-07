package com.example.eventrese.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eventrese.fragments.EventDetailFragment;
import com.example.eventrese.models.Event;

import java.util.List;

public class EventPagerAdapter extends FragmentPagerAdapter {
    private List<Event> mEvents;

    public EventPagerAdapter(FragmentManager fm, List<Event> events){
        super(fm);
        mEvents = events;
    }

    @Override
    public Fragment getItem(int position){
        return EventDetailFragment.newInstance(mEvents.get(position));
    }

    @Override
    public int getCount(){
        return mEvents.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mEvents.get(position).getName();
    }
}
