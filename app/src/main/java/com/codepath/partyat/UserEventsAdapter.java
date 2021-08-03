package com.codepath.partyat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class UserEventsAdapter extends RecyclerView.Adapter<UserEventsAdapter.ViewHolder> {
    private Context mContext;
    private List<Event> mEvents;
    private Activity mActivity;

    public UserEventsAdapter(Context context, List<Event> events, Activity activity) {
        this.mContext = context;
        this.mEvents = events;
        this.mActivity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event post = mEvents.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.ivUserPost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DetailsActivity.class);
                    //get event at this position
                    Event event = mEvents.get(getAdapterPosition());
                    // serialize the movie using parceler
                    i.putExtra("event", (Serializable) event);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mActivity, (View)mIvImage, "image");
                    mContext.startActivity(i, options.toBundle());
                }
            });
        }

        public void bind(Event event) {
            if (event.getImage() != null ) {
                Glide.with(mContext).load(event.getImage().getUrl()).into(mIvImage);
            }
        }

    }
}
