package com.example.eventrese.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.adapters.EventPagerAdapter;
import com.example.eventrese.models.Event;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager) ViewPager mViewPager;
    private EventPagerAdapter adapterViewPager;
    private String mSource;
    List<Event> mEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ButterKnife.bind(this);

        mEvents = Parcels.unwrap(getIntent().getParcelableExtra(Constants.EXTRA_KEY_EVENTS));
        int startingPosition = getIntent().getIntExtra(Constants.EXTRA_KEY_POSITION, 0);
        mSource = getIntent().getStringExtra(Constants.KEY_SOURCE);

        adapterViewPager = new EventPagerAdapter(getSupportFragmentManager(), mEvents, mSource);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}