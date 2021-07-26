package com.codepath.partyat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends FeedFragment {
    private static final String TAG = "WishlistFragment";
//    private RecyclerView mRvWishlist;
//    private List<Event> mEvents;
//    private EventAdapter mAdapter;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void queryEvents() {
//        super.queryEvents();
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include("user");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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