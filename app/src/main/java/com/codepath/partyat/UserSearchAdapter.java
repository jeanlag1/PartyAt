package com.codepath.partyat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {
    private Context mContext;
    private List<ParseUser> mUsers;
    private DataManager mDataManager;

    public UserSearchAdapter(Context context, List<ParseUser> users, Activity activity) {
        this.mContext = context;
        this.mUsers = users;
        mDataManager = new DataManager(activity, context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = mUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mUsername;
        ToggleButton mFollowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.userSearchImg);
            mFollowBtn = itemView.findViewById(R.id.btnFolllow);
            mUsername = itemView.findViewById(R.id.tvSearchUsername);
            mFollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataManager.followAction(ParseUser.getCurrentUser(), mUsers.get(getAdapterPosition()));
                }
            });
        }

        public void bind(ParseUser user) {
            mUsername.setText(user.getUsername());
            mFollowBtn.setChecked(mDataManager.isFollowing(ParseUser.getCurrentUser(),user));
            String url = user.getParseFile("profileImage").getUrl();
            Glide.with(mContext).load(url).into(mIvImage);
        }

    }
}
