package com.codepath.partyat.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.parse.ParseObject;
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

        EventAdapter.OnLongClickListener onLongClickListener = new EventAdapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("This party will be deleted!")
                        .setMessage("Are you sure?");

                // Add the buttons
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(position);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        // Set up Adapter
        mAdapter = new EventAdapter(getContext(), mEvents, getActivity(), "wishlist", onLongClickListener);
        mRvWishlist.setAdapter(mAdapter);
        mRvWishlist.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create new list
        List<Event> liked;
        // fetch current array
        liked = new ArrayList<>();
        //old list
        List<Event> old = ParseUser.getCurrentUser().getList("liked");
        if (old != null) {
            liked.addAll(old);
        }
        Log.i(TAG, " Elements : " + liked.get(0).getObjectId());
        mEvents.addAll(liked);
        mAdapter.notifyDataSetChanged();
    }

    private void removeItem(int position) {
        List<Event> liked = ParseUser.getCurrentUser().getList("liked");
        liked.remove(mEvents.get(position));
        ParseUser.getCurrentUser().put("liked", liked);
        ParseUser.getCurrentUser().saveInBackground();
        mEvents.remove(position);
        mAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(), "Item  was removed", Toast.LENGTH_SHORT).show();

    }
}