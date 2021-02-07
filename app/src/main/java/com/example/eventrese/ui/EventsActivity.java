package com.example.eventrese.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.adapters.EventListAdapter;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.EventSearch;
import com.example.eventrese.network.YelpApi;
import com.example.eventrese.network.YelpService;
import com.example.eventrese.util.OnEventSelectedListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity implements OnEventSelectedListener {
    private Integer mPosition;
    List<Event> mEvents;
    String mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        if (savedInstanceState != null){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                mEvents = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_EVENTS));
                mSource = savedInstanceState.getString(Constants.KEY_SOURCE);

                if (mPosition != null && mEvents != null){
                    Intent intent = new Intent(this, EventDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                    intent.putExtra(Constants.EXTRA_KEY_EVENTS, Parcels.wrap(mEvents));
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        if (mPosition != null && mEvents != null){
            outState.putInt(Constants.EXTRA_KEY_POSITION, mPosition);
            outState.putParcelable(Constants.EXTRA_KEY_EVENTS, Parcels.wrap(mEvents));
            outState.putString(Constants.KEY_SOURCE, mSource);
        }
    }

    @Override
    public void onEventSelected(Integer position, List<Event> events, String source){
        mPosition = position;
        mEvents = events;
        mSource = source;
    }

}