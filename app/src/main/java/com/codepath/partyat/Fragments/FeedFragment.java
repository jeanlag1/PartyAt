package com.codepath.partyat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.partyat.Event;
import com.codepath.partyat.EventAdapter;
import com.codepath.partyat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
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
    }
}