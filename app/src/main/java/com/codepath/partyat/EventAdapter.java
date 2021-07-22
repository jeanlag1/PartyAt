package com.codepath.partyat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context mContext;
    List<Event> mEvents;

    public EventAdapter(Context context, List<Event> events) {
        this.mContext = context;
        this.mEvents = events;
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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mDetails;
        private ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tvEventTitle);
            mDetails = itemView.findViewById(R.id.tvEventDetails);
            mImage = itemView.findViewById(R.id.ivEventImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DetailsActivity.class);
                    //get event at this position
                    Event event = mEvents.get(getAdapterPosition());
                    // serialize the movie using parceler
                    i.putExtra("event", (Serializable) event);
                    mContext.startActivity(i);
                }
            });
        }

        public void bind(Event event) {
            mTitle.setText(event.getTitle());
            mDetails.setText(event.getDetails());
            if (event.getImage() != null) {
                Glide.with(mContext).load(event.getImage().getUrl()).into(mImage);
            }
        }
    }
}
