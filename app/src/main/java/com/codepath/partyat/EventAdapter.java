package com.codepath.partyat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseException;

import java.io.Serializable;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }
    OnLongClickListener longClickListener;
    private Context mContext;
    private List<Event> mEvents;
    private DataManager mDataManager;
    private Activity mActivity;
    private String mFragmentName;


    public EventAdapter(Context context, List<Event> events, Activity activity, String mFragmentName, OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        this.mContext = context;
        this.mEvents = events;
        this.mActivity = activity;
        this.mFragmentName = mFragmentName;
        this.mDataManager = new DataManager(activity, context);
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        try {
            holder.bind(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDetails;
        private ImageView mImage;
        private ImageView mProfileImg;
        private TextView mUsername;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tvEventTitle);
            mDetails = itemView.findViewById(R.id.tvEventDetails);
            mUsername = itemView.findViewById(R.id.tvPostUser);
            mProfileImg = itemView.findViewById(R.id.ivPostImg);
            mImage = itemView.findViewById(R.id.ivEventImg);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (mFragmentName == "wishlist") {
                            longClickListener.onItemLongClicked(getAdapterPosition());
                        }
                        super.onLongPress(e);
                    }


                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if(mFragmentName== "wishlist") {
                            return false;
                        }
                        mDataManager.addPartyToWishlist(mEvents.get(getAdapterPosition()));
                        Toast.makeText(mContext, "Double", Toast.LENGTH_SHORT).show();
                        return super.onDoubleTap(e);
                    }
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Intent i = new Intent(mContext, DetailsActivity.class);
                        //get event at this position
                        Event event = mEvents.get(getAdapterPosition());
                        // serialize the movie using parceler
                        i.putExtra("event", (Serializable) event);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(mActivity, (View)mImage, "image");
                        mContext.startActivity(i, options.toBundle());
                        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                        return super.onSingleTapConfirmed(e);
                    }

                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }

        public void bind(Event event) throws ParseException {
            mTitle.setText(event.getTitle());
            mDetails.setText(event.getDetails());
            mUsername.setText(event.getUser().fetchIfNeeded().getUsername());
            Glide.with(mContext).load(event.getUser().getParseFile("profileImage").getUrl()).transform(new RoundedCorners(300)).into(mProfileImg);
            if (event.getImage() != null) {
                Glide.with(mContext).load(event.getImage().getUrl()).into(mImage);
            }
        }
    }
}
