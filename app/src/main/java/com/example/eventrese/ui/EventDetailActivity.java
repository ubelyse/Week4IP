package com.example.eventrese.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.eventrese.R;
import com.example.eventrese.adapters.EventPagerAdapter;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.Events;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager) ViewPager mViewPager;
    private EventPagerAdapter adapterViewPager;
    ArrayList<Event> mevents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        mevents = Parcels.unwrap(getIntent().getParcelableExtra("event"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new EventPagerAdapter(getSupportFragmentManager(), mevents);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}