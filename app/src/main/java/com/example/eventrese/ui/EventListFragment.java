package com.example.eventrese.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventrese.Constants;
import com.example.eventrese.R;
import com.example.eventrese.adapters.EventListAdapter;
import com.example.eventrese.models.Event;
import com.example.eventrese.models.EventSearch;
import com.example.eventrese.network.YelpApi;
import com.example.eventrese.network.YelpService;
import com.example.eventrese.util.OnEventSelectedListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private EventListAdapter mAdapter;
    private ArrayList<Event> events = new ArrayList<>();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;
    private OnEventSelectedListener mOnEventSelectedListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mOnEventSelectedListener = (OnEventSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();
        // Instructs fragment to include menu options:
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, view);
        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
        if (mRecentAddress != null) {
            getEvents(mRecentAddress);
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void getEvents(String location){
        YelpApi client = YelpService.getClient();

        retrofit2.Call<EventSearch> call = client.getEvents(location, "event");

        call.enqueue(new Callback<EventSearch>() {
            @Override
            public void onResponse(retrofit2.Call<EventSearch> call, Response<EventSearch> response) {
                if (response.isSuccessful()) {
                    events = (ArrayList<Event>) response.body().getEvents();
                    mAdapter = new EventListAdapter(getActivity(), events,mOnEventSelectedListener);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    showEvents();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong. Please check your Internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventSearch> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong. Please check your Internet connection and try again later", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    // Method is now void, menu inflater is now passed in as argument:
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        // Call super to inherit method from parent:
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                addToSharedPreferences(s);
                getEvents(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void showEvents() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }

}