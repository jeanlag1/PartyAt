package com.codepath.partyat.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.partyat.DataManager;
import com.codepath.partyat.Event;
import com.codepath.partyat.EventAdapter;
import com.codepath.partyat.Preference;
import com.codepath.partyat.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

public class FeedFragment extends Fragment implements DataManager.EventsQueryCallback {

    private static final String TAG = "FeedFragment";
    private static final int SCORE_SCALE_FACTOR = 10000;
    private RecyclerView rvFeed;
    private List<Event> mEvents;
    private EventAdapter mAdapter;
    private Preference mUserPref;
    ParseUser mCurrentUser;
    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLocation;
    private DataManager mDataManager;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataManager = new DataManager(getActivity());
        rvFeed = view.findViewById(R.id.rvFeed);
        mEvents = new ArrayList<>();
        mUserPref = new Preference();
        mCurrentUser = ParseUser.getCurrentUser();

        mDataManager.queryPreferences(this);

        mAdapter = new EventAdapter(getContext(), mEvents);
        rvFeed.setAdapter(mAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onEventsFetch(List<Event> events) {

        // Sort events based on user preferences
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return (int) - (mDataManager.compatibilityScore(o1) - mDataManager.compatibilityScore(o2));
            }
        });

        mEvents.addAll(events);
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }
}