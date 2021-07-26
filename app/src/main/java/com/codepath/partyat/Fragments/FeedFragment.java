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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";
    RecyclerView rvFeed;
    List<Event> mEvents;
    EventAdapter mAdapter;
    private Preference mUserPref;
    ParseUser mCurrentUser;
    private FusedLocationProviderClient fusedLocationClient;
    private Location mCurrentLocation;

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
        rvFeed = view.findViewById(R.id.rvFeed);
        mEvents = new ArrayList<>();
        mCurrentUser = ParseUser.getCurrentUser();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getLocation();

        mAdapter = new EventAdapter(getContext(), mEvents);
        rvFeed.setAdapter(mAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        queryEvents();
        queryPreferences();

    }

    @SuppressWarnings({"MissingPermission"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mCurrentLocation = location;
                        }
                    }
                });
    }

    private void queryPreferences() {
        ParseQuery<Preference> query = ParseQuery.getQuery(Preference.class);
        query.include("user");
        query.whereEqualTo("user", mCurrentUser);
        query.findInBackground(new FindCallback<Preference>() {
            @Override
            public void done(List<Preference> prefs, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting pref", e);
                    return;
                }
                mUserPref = prefs.get(0);
            }
        });

    }

    public void queryEvents() {
        // TODO: implementation to fetch current available events
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include("user");
        // Configure limit and sort order
//        query.setLimit(MAX_POST_TO_SHOW);
        // get the latest 20 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                Log.i(TAG, events.toString());
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting events", e);
                    return;
                }
                Toast.makeText(getContext(), "Successfully fetched", Toast.LENGTH_SHORT).show();
                // for debugging purposes let's print every post description to logcat
//                Log.d(TAG, "Class of event: " + events.get(0).getClass());
                for (Event event : events) {
                    Log.i(TAG, "Event: " + event.getTitle());
                }

                // Sort events based on user preferences
//                events.sort(new Comparator<Event>() {
//                    @Override
//                    public int compare(Event o1, Event o2) {
//                        return (int) - (compatibilityScore(o1) - compatibilityScore(o2));
//                    }
//                });
                mEvents.addAll(events);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /* This method returns a score describing how compatible
     *  a given event is to the current user and helps sorting
     *  the main feed.
     *  The higher the score, the more compatible the event is.
     */
    private double compatibilityScore(Event o1) {

        // retrieve data
        int max_distance = mUserPref.getMaxDistance();
        boolean is_weekend = mUserPref.getIsWeekend();
        boolean is_private = mUserPref.getIsPrivate();

        double dist_to_event = computeDistToEvent(o1.getLocation());

        // factors
        double dist_factor = (max_distance - dist_to_event) / max_distance;
        int event_type = o1.getIsPrivate() ? 1 : 0;
        int event_time = o1.getIsWeekend() ? 1 : 0;
        int weekend_factor = is_weekend ? event_time : 0;
        int private_factor = is_private ? event_type : 0;
        return weekend_factor + private_factor + dist_factor;
    }

    private boolean isWeekDay(String date) {
    }

    // This method computes the Euclidean distance between two location points
    private double computeDistToEvent(ParseGeoPoint o1) {
        return Math.sqrt(Math.pow(o1.getLatitude() - mCurrentLocation.getLatitude(), 2) 
                + Math.pow(o1.getLongitude() - mCurrentLocation.getLongitude(), 2));
    }
}