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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment {
    private static final String TAG = "WishlistFragment";
    private RecyclerView mRvWishlist;
    private List<Event> mEvents;
    private EventAdapter mAdapter;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvWishlist = view.findViewById(R.id.rvWishlist);
        mEvents = new ArrayList<>();
        mAdapter = new EventAdapter(getContext(), mEvents);
        mRvWishlist.setAdapter(mAdapter);
        mRvWishlist.setLayoutManager(new LinearLayoutManager(getContext()));

//        Log.i(TAG, "First element: " + ParseUser.getCurrentUser().getList("liked").get(0).getTitle());
        List<Event> liked = null;
        try {
            liked = ParseUser.getCurrentUser().fetchIfNeeded().getList("liked");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (liked != null){
            mEvents.addAll(liked);
            mAdapter.notifyDataSetChanged();
        }
    }
}