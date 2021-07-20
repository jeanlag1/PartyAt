package com.codepath.partyat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.codepath.partyat.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";
    RecyclerView rvFeed;
    List<Event> mEvents;
    EventAdapter mAdapter;

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

        mAdapter = new EventAdapter(getContext(), mEvents);
        rvFeed.setAdapter(mAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        queryEvents();

    }

    private void queryEvents() {
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
//                 save received posts to list and notify adapter of new data
                mEvents.addAll(events);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}