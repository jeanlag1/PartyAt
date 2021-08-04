package com.codepath.partyat.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.partyat.DataManager;
import com.codepath.partyat.Event;
import com.codepath.partyat.Preference;
import com.codepath.partyat.PreferencesActivity;
import com.codepath.partyat.R;
import com.codepath.partyat.User;
import com.codepath.partyat.UserEventsAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProfileFragment extends Fragment implements DataManager.EventsQueryCallback{
    private ImageView mImage;
    private TextView mFollowing;
    private TextView mCreation;
    private TextView mUsername;
    private RecyclerView mRvCreations;
    private ImageButton mBtnSettings;
    private DataManager mDataManager;
    private List<Event> mEvents;
    private ParseUser mCurrentUser;
    private UserEventsAdapter mAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnSettings = view.findViewById(R.id.btnSettings);
        mCreation = view.findViewById(R.id.tvCreation);
        mFollowing = view.findViewById(R.id.tvFollowing);
        mImage = view.findViewById(R.id.ivImageProfile);
        mRvCreations = view.findViewById(R.id.rvCreations);
        mUsername = view.findViewById(R.id.tvUsername);
        mDataManager = new DataManager(getActivity(),getContext());
        mCurrentUser = ParseUser.getCurrentUser();
        mEvents = new ArrayList<>();

        setAdapter();
        setViews();
        mDataManager.queryEvents(this, true, false);
    }

    private void setViews() {
        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });
        mFollowing.setText(mCurrentUser.getList("following") != null
                ? String.valueOf(mCurrentUser.getList("following").size()) : "0");
        mUsername.setText("@" + mCurrentUser.getUsername());
//        if (mCurrentUser.getParseFile("profileImage") != null) {
        String url = mCurrentUser.getParseFile("profileImage").getUrl();
        Glide.with(getContext()).load(url).into(mImage);
//        }
    }

    private void goToSettings() {
        Intent i = new Intent(getContext(), PreferencesActivity.class);
        startActivity(i);
    }


    @Override
    public void onEventsFetch(List<Event> events) throws ParseException {
        mEvents.addAll(events);
        mCreation.setText(String.valueOf(mEvents.size()));
        mAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        mAdapter = new UserEventsAdapter(getContext(), mEvents, getActivity());
        mRvCreations.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRvCreations.setAdapter(mAdapter);

    }
}